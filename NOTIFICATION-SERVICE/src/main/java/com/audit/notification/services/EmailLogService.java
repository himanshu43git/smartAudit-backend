package com.audit.notification.services;

import com.audit.notification.model.EmailNotificationLog;
import com.audit.notification.RabbitMQ.template.EmailTemplate;

import java.util.List;

public interface EmailLogService {

    void saveEmailNotificationLog(EmailNotificationLog emailNotificationLog);

    List<EmailNotificationLog> getAllEmailNotificationLogs();

    List<EmailNotificationLog> getEmailNotificationLogsByRecipientEmail(String recipientEmail);

    List<EmailNotificationLog> getEmailNotificationLogsByStatus(String status);

    List<EmailNotificationLog> getEmailNotificationLogsByCreatedAtBetween(String start, String end);

    List<EmailNotificationLog> getEmailNotificationLogsBySentAtBetween(String start, String end);

    boolean updateEmailNotificationLogStatus(String id, String status);

    boolean deleteEmailNotificationLog(String id);

    boolean deleteAllEmailNotificationLogs();

}
