package com.audit.auth.services;

import com.audit.auth.events.OtpEvent;
import com.audit.auth.events.WelcomeEmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMqService {

    private final StreamBridge bridge;

    public RabbitMqService(StreamBridge bridge) {
        this.bridge = bridge;
    }

    public void sendWelcomeEmailEvent(WelcomeEmailEvent welcomeEmailEvent){

        boolean response = bridge.send("WelcomeEmailEvent-out-0", welcomeEmailEvent);

        if(!response){
            log.error("Failed to send WelcomeEmailEvent to RabbitMQ for recipient: {}", welcomeEmailEvent.getRecipientEmail());
            throw new RuntimeException("Failed to send WelcomeEmailEvent to RabbitMQ");
        }

        log.info("Successfully sent WelcomeEmailEvent to RabbitMQ for recipient: {}", welcomeEmailEvent.getRecipientEmail());

    }


    public void sendOtpEmail(OtpEvent event){

        boolean response = bridge.send("OtpEvent-out-0", event);

        if(!response){
            log.error("Failed to send OTP to RabbitMQ for recipient: {}", event.getRecipientEmail());
            throw new RuntimeException("Failed to send WelcomeEmailEvent to RabbitMQ");
        }

        log.info("Successfully sent OTP to RabbitMQ for recipient: {}", event.getRecipientEmail());

    }


    public void sendResetPasswordEmail(String email){

        boolean response = bridge.send("ResetPasswordEvent-out-0", email);

        if(!response){
            log.error("Failed to send Reset Password Email to RabbitMQ for recipient: {}", email);
            throw new RuntimeException("Failed to send Reset Password Email to RabbitMQ");
        }

        log.info("Successfully sent Reset Password Email to RabbitMQ for recipient: {}", email);

    }

}
