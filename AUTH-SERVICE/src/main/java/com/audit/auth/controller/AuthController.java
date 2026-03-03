package com.audit.auth.controller;

import com.audit.auth.io.request.LoginRequest;
import com.audit.auth.io.request.OtpGenerateRequest;
import com.audit.auth.io.request.OtpVerifyRequest;
import com.audit.auth.io.request.RefreshTokenRequest;
import com.audit.auth.io.request.UserRequest;
import com.audit.auth.io.response.AuthTokensResponse;
import com.audit.auth.io.response.OtpGenerateResponse;
import com.audit.auth.io.response.OtpVerifyResponse;
import com.audit.auth.io.response.UserResponse;
import com.audit.auth.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.adduser(request));
    }

    @PostMapping("/otp/generate")
    public ResponseEntity<OtpGenerateResponse> generateOtp(@Valid @RequestBody OtpGenerateRequest request) {
        return ResponseEntity.ok(authService.generateOtp(request));
    }

    @PostMapping("/otp/verify")
    public ResponseEntity<OtpVerifyResponse> verifyOtp(@Valid @RequestBody OtpVerifyRequest request) {
        return ResponseEntity.ok(authService.verifyOtp(request));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<AuthTokensResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<AuthTokensResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

//    @PostMapping("/testRabbit")
//    public ResponseEntity<String> testRabbit(@RequestBody UserRequest request) {
//        return ResponseEntity.ok(authService.testRabbit(request));
//    }

}
