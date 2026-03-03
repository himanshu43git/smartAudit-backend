package com.audit.notification.RabbitMQ.events;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
//import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WelcomeEmailEvent {

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Recipient email should be valid")
    private String recipientEmail;

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Recipient email should be valid")
    private String senderEmail;

    @NotBlank(message = "Recipient name is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Recipient name must contain only letters and spaces")
    private String recipientName;
}
