package com.audit.auth.services.impl;

import com.audit.auth.events.OtpEvent;
import com.audit.auth.events.WelcomeEmailEvent;
import com.audit.auth.exceptions.BadRequestException;
import com.audit.auth.exceptions.UnauthorizedException;
import com.audit.auth.exceptions.UserAlreadyExistsException;
import com.audit.auth.exceptions.UserNotFoundException;
import com.audit.auth.io.request.*;
import com.audit.auth.io.response.AuthTokensResponse;
import com.audit.auth.io.response.OtpGenerateResponse;
import com.audit.auth.io.response.OtpVerifyResponse;
import com.audit.auth.io.response.UserResponse;
import com.audit.auth.model.OtpChannel;
import com.audit.auth.model.User;
import com.audit.auth.repository.AuthRepository;
import com.audit.auth.security.JwtService;
import com.audit.auth.services.AuthService;
import com.audit.auth.services.RabbitMqService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final StreamBridge bridge;

    private final RabbitMqService rabbitMqService;

    @Value("${notification.email.from}")
    String fromEmail;

    private final SecureRandom secureRandom = new SecureRandom();

    private final int otpLength;
    private final long otpTtlSeconds;
    private final boolean exposeOtpInResponse;
    private final int otpMaxAttempts;

    public AuthServiceImpl(
            AuthRepository authRepository,
            PasswordEncoder passwordEncoder,
            @Value("${auth.otp.length:6}") int otpLength,
            @Value("${auth.otp.ttl-seconds:300}") long otpTtlSeconds,
            @Value("${auth.otp.expose-in-response:false}") boolean exposeOtpInResponse,
            @Value("${auth.otp.max-attempts:5}") int otpMaxAttempts,
            JwtService jwtService,
            StreamBridge bridge,
            RabbitMqService rabbitMqService
    ) {
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpLength = otpLength;
        this.otpTtlSeconds = otpTtlSeconds;
        this.exposeOtpInResponse = exposeOtpInResponse;
        this.otpMaxAttempts = otpMaxAttempts;
        this.jwtService = jwtService;
        this.bridge = bridge;
        this.rabbitMqService = rabbitMqService;
    }

    // ================= REGISTER =================
    @Override
    public UserResponse adduser(UserRequest request) {

        if (authRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists with email");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .isVerified(false)
                .otpAttempts(0)
                .otpUsed(false)
                .build();

        User saved = authRepository.save(user);

        // Publish UserCreatedEvent to RabbitMQ
        this.syncToProfileService(request);

        //publish Welcome Email Notification to RabbitMQ
        WelcomeEmailEvent welcomeEmailEvent = WelcomeEmailEvent.builder()
                .recipientEmail(saved.getEmail())
                .senderEmail(fromEmail) // using email as name for simplicity
                .build();

        rabbitMqService.sendWelcomeEmailEvent(welcomeEmailEvent);


        return UserResponse.builder()
                .id(saved.getId().toString())
                .email(saved.getEmail())
                .phoneNumber(saved.getPhoneNumber())
                .isVerified(saved.isVerified())
                .createdAt(saved.getCreatedAt().toString())
                .updatedAt(saved.getUpdatedAt().toString())
                .build();
    }

    // ================= OTP GENERATE =================
    @Override
    public OtpGenerateResponse generateOtp(OtpGenerateRequest request) {

        String email = normalize(request.getEmail());
        String phone = normalize(request.getPhoneNumber());

        if (email == null && phone == null) {
            throw new BadRequestException("Email or phone required");
        }

        User user = (email != null)
                ? authRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"))
                : authRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        OtpChannel channel = request.getChannel();
        if (channel == null) {
            channel = phone != null ? OtpChannel.SMS : OtpChannel.EMAIL;
        }

        String otp = generateOtpValue();
        Instant now = Instant.now();

        user.setHashedOtp(passwordEncoder.encode(otp));
        user.setOtpGeneratedAt(now);
        user.setOtpExpiry(now.plusSeconds(otpTtlSeconds));
        user.setOtpAttempts(0);
        user.setOtpUsed(false);
        user.setOtpChannel(channel);
        user.setOtpVerifiedAt(null);

        authRepository.save(user);

        OtpEvent event = OtpEvent.builder()
                .senderEmail(fromEmail)
                .recipientEmail(user.getEmail())
                .otp(otp)
                .build();

        rabbitMqService.sendOtpEmail(event);

        return OtpGenerateResponse.builder()
                .userId(user.getId().toString())
                .channel(channel)
                .maskedDestination(
                        email != null ? maskEmail(email) : maskPhone(phone)
                )
                .expiresAt(user.getOtpExpiry().toString())
                .otp(exposeOtpInResponse ? otp : null)
                .build();
    }

    // ================= OTP VERIFY =================
    @Override
    public OtpVerifyResponse verifyOtp(OtpVerifyRequest request) {

        String email = normalize(request.getEmail());
        String phone = normalize(request.getPhoneNumber());

        User user = (email != null)
                ? authRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid OTP"))
                : authRepository.findByPhoneNumber(phone)
                .orElseThrow(() -> new UnauthorizedException("Invalid OTP"));

        if (user.getHashedOtp() == null || user.getOtpExpiry().isBefore(Instant.now())) {
            throw new UnauthorizedException("OTP expired");
        }

        if (Boolean.TRUE.equals(user.getOtpUsed())) {
            throw new UnauthorizedException("OTP already used");
        }

        if (user.getOtpAttempts() >= otpMaxAttempts) {
            throw new UnauthorizedException("OTP attempts exceeded");
        }

        if (!passwordEncoder.matches(request.getOtp(), user.getHashedOtp())) {
            user.setOtpAttempts(user.getOtpAttempts() + 1);
            authRepository.save(user);
            throw new UnauthorizedException("Invalid OTP");
        }

        user.setOtpUsed(true);
        user.setVerified(true);
        user.setOtpVerifiedAt(Instant.now());

        // cleanup
        user.setHashedOtp(null);
        user.setOtpExpiry(null);
        user.setOtpGeneratedAt(null);
        user.setOtpAttempts(0);

        authRepository.save(user);

        this.syncVerifyToProfileService(user.getEmail());

        return OtpVerifyResponse.builder()
                .userId(user.getId().toString())
                .verified(true)
                .verifiedAt(user.getOtpVerifiedAt().toString())
                .build();
    }

    // ================= AUTH =================
    @Override
    public AuthTokensResponse login(LoginRequest request) {

        User user = authRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return jwtService.issueTokens(user);
    }

    @Override
    public AuthTokensResponse refresh(RefreshTokenRequest request) {
        var userId = jwtService.validateRefreshTokenAndGetUserId(request.getRefreshToken());
        User user = authRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Invalid token"));
        return jwtService.issueTokens(user);
    }



    public void syncToProfileService(UserRequest request) {

        System.out.println("Sending message to RabbitMQ: " + request);

        boolean response = bridge.send("UserCreatedEvent-out-0", request);
        if(response){
            System.out.println("Message sent to RabbitMQ successfully");
//            return "Message sent successfully";
        }else{
            System.out.println("Message not sent");
//            return "Message not sent";
        }

    }

    public void syncVerifyToProfileService(String email) {

        System.out.println("Sending verification message to RabbitMQ for email: " + email);

        boolean response = bridge.send("UserVerifiedEvent-out-0", email);
        if(response){
            System.out.println("Verification message sent to RabbitMQ successfully");
//            return "Verification message sent successfully";
        }else{
            System.out.println("Verification message not sent");
//            return "Verification message not sent";
        }

    }


    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {

        User user = authRepository.findById(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getEmail().equals(request.getEmail())) {
            throw new UnauthorizedException("Email does not match user");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        authRepository.save(user);

        rabbitMqService.sendResetPasswordEmail(user.getEmail());

        return ResetPasswordResponse.builder()
                .userId(user.getId().toString())
                .email(user.getEmail())
                .message("Password reset successful")
                .build();

    }

    // ================= HELPERS =================
    private String generateOtpValue() {
        int min = (int) Math.pow(10, otpLength - 1);
        int max = (int) Math.pow(10, otpLength);
        return String.valueOf(min + secureRandom.nextInt(max - min));
    }

    private String normalize(String v) {
        return (v == null || v.isBlank()) ? null : v.trim();
    }

    private String maskEmail(String email) {
        int at = email.indexOf('@');
        return at <= 1 ? "***" : email.charAt(0) + "***" + email.substring(at);
    }

    private String maskPhone(String phone) {
        String d = phone.replaceAll("\\D", "");
        return d.length() <= 4 ? "****" : "****" + d.substring(d.length() - 4);
    }
}
