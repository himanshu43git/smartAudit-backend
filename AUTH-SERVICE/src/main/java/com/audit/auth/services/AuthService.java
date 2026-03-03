package com.audit.auth.services;

import com.audit.auth.io.request.LoginRequest;
import com.audit.auth.io.request.OtpGenerateRequest;
import com.audit.auth.io.request.OtpVerifyRequest;
import com.audit.auth.io.request.RefreshTokenRequest;
import com.audit.auth.io.request.UserRequest;
import com.audit.auth.io.response.AuthTokensResponse;
import com.audit.auth.io.response.OtpGenerateResponse;
import com.audit.auth.io.response.OtpVerifyResponse;
import com.audit.auth.io.response.UserResponse;

public interface AuthService {

    UserResponse adduser(UserRequest request);

    OtpGenerateResponse generateOtp(OtpGenerateRequest request);

    OtpVerifyResponse verifyOtp(OtpVerifyRequest request);

    AuthTokensResponse login(LoginRequest request);

    AuthTokensResponse refresh(RefreshTokenRequest request);

}
