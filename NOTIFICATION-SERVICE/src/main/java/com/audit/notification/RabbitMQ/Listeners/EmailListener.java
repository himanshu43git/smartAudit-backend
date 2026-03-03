package com.audit.notification.RabbitMQ.Listeners;

import com.audit.notification.RabbitMQ.events.OtpEvent;
import com.audit.notification.RabbitMQ.events.WelcomeEmailEvent;
import com.audit.notification.RabbitMQ.template.EmailTemplate;
import com.audit.notification.RabbitMQ.template.OtpTemplate;
import com.audit.notification.services.impl.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class EmailListener {

    private final EmailService emailService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public EmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Bean
    public Consumer<WelcomeEmailEvent> WelcomeEmailEvent() {

        return event -> {

            EmailTemplate emailTemplate = EmailTemplate.builder()
                    .recipientEmail(event.getRecipientEmail())
                    .senderEmail(event.getSenderEmail())
                    .body("Thank you for registering with our platform. We are excited to have you on board!")
                    .subject("Welcome to our platform")
                    .name(event.getRecipientName())
                    .build();

            emailService.sendWelcomeEmail(emailTemplate);
        };
    }

    @Bean
    public Consumer<OtpEvent> OtpEvent(){

        return event -> {
            OtpTemplate template = OtpTemplate.builder()
                    .recipientEmail(event.getRecipientEmail())
                    .senderEmail(event.getSenderEmail())
                    .subject("Otp verification")
                    .otp(event.getOtp())
                    .build();

            emailService.sendOtpEmail(template, "send-otp");
        };
    }

    @Bean
    public Consumer<OtpEvent> ResetPasswordEvent(){

        System.out.println("reset password event raised");

        return event -> {

            OtpTemplate template = OtpTemplate.builder()
                    .recipientEmail(event.getRecipientEmail())
                    .senderEmail(event.getSenderEmail())
                    .subject("Password Reset OTP")
                    .otp(event.getOtp())
                    .build();

            emailService.sendOtpEmail(template, "Reset-password-email");

        };

    }

    //TODO: add an Spring Function to send the reports and some more endpoints to be added via RabbitMQ Queue

}
