package com.audit.notification.RabbitMQ.template;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailTemplate {

    private String subject;

    private String body;

    private String recipientEmail;

    private String senderEmail;

    private String name;

}
