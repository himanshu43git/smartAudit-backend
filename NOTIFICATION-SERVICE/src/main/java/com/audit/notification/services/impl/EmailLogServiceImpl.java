package com.audit.notification.services.impl;

import com.audit.notification.model.EmailNotificationLog;
import com.audit.notification.RabbitMQ.template.EmailTemplate;
import com.audit.notification.repositories.EmailLogRepository;
import com.audit.notification.services.EmailLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailLogServiceImpl implements EmailLogService {

    private final EmailLogRepository emailLogRepository;

    public EmailLogServiceImpl(EmailLogRepository emailLogRepository) {
        this.emailLogRepository = emailLogRepository;
    }

    @Override
    public void saveEmailNotificationLog(EmailNotificationLog emailNotificationLog) {

        if(emailNotificationLog == null) {
            throw new IllegalArgumentException("EmailNotificationLog cannot be null");
        }

        emailLogRepository.save(emailNotificationLog);

    }

    @Override
    public List<EmailNotificationLog> getAllEmailNotificationLogs() {
        return List.of();
    }

    @Override
    public List<EmailNotificationLog> getEmailNotificationLogsByRecipientEmail(String recipientEmail) {
        return List.of();
    }

    @Override
    public List<EmailNotificationLog> getEmailNotificationLogsByStatus(String status) {
        return List.of();
    }

    @Override
    public List<EmailNotificationLog> getEmailNotificationLogsByCreatedAtBetween(String start, String end) {
        return List.of();
    }

    @Override
    public List<EmailNotificationLog> getEmailNotificationLogsBySentAtBetween(String start, String end) {
        return List.of();
    }

    @Override
    public boolean updateEmailNotificationLogStatus(String id, String status) {
        return false;
    }

    @Override
    public boolean deleteEmailNotificationLog(String id) {
        return false;
    }

    @Override
    public boolean deleteAllEmailNotificationLogs() {
        return false;
    }
}
