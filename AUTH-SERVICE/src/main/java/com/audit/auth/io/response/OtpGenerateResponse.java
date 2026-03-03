package com.audit.auth.io.response;

import com.audit.auth.model.OtpChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpGenerateResponse {

    private String userId;

    private OtpChannel channel;

    private String maskedDestination;

    private String expiresAt;

    private String otp;
}
