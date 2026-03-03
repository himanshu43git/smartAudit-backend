package com.audit.notification.services;

import com.audit.notification.model.SmsNotificationLog;
import com.audit.notification.model.SmsTemplate;

import java.util.List;

public interface SmsLogService {

    void saveSmsNotificationLog(SmsNotificationLog smsNotificationLog);

    boolean sendSmsNotification(SmsTemplate smsTemplate);

    boolean updateSmsNotificationLogStatus(String id, String status);

    boolean deleteSmsNotificationLog(String id);

    boolean deleteAllSmsNotificationLogs();

    List<SmsNotificationLog> getAllSmsNotificationLogs();

    List<SmsNotificationLog> getSmsNotificationLogsByRecipientPhoneNumber(String recipientPhoneNumber);

    List<SmsNotificationLog> getSmsNotificationLogsByStatus(String status);

    List<SmsNotificationLog> getSmsNotificationLogsByCreatedAtBetween(String start, String end);

    List<SmsNotificationLog> getSmsNotificationLogsBySentAtBetween(String start, String end);

    List<SmsNotificationLog> getSmsNotificationLogsByUpdatedAtBetween(String start, String end);

}
