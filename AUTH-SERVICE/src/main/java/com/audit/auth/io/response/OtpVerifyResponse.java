package com.audit.auth.io.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpVerifyResponse {

    private String userId;

    private boolean verified;

    private String verifiedAt;
}
