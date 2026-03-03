package com.audit.notification.RabbitMQ.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OtpTemplate {

    private String recipientEmail;

    private String senderEmail;

    private String otp;

    private String subject;

}
