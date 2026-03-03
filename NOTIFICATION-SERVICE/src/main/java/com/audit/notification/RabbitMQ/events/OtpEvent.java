package com.audit.notification.RabbitMQ.events;

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

}
