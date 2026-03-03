package com.audit.auth.events;

import com.audit.auth.model.OtpChannel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpEvent {

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Recipient email should be valid")
    private String recipientEmail;

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Recipient email should be valid")
    private String senderEmail;

    @NotBlank(message = "OTP is required")
    private String otp;

//    @Enumerated(EnumType.STRING)
//    @NotBlank(message = "channel cannot be empty")
//    private OtpChannel channel;

}
