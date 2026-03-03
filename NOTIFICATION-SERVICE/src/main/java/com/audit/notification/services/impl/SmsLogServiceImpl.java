package com.audit.notification.services.impl;

import com.audit.notification.model.SmsNotificationLog;
import com.audit.notification.model.SmsTemplate;
import com.audit.notification.services.SmsLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmsLogServiceImpl implements SmsLogService {
    @Override
    public void saveSmsNotificationLog(SmsNotificationLog smsNotificationLog) {

    }

    @Override
    public boolean sendSmsNotification(SmsTemplate smsTemplate) {
        return false;
    }

    @Override
    public boolean updateSmsNotificationLogStatus(String id, String status) {
        return false;
    }

    @Override
    public boolean deleteSmsNotificationLog(String id) {
        return false;
    }

    @Override
    public boolean deleteAllSmsNotificationLogs() {
        return false;
    }

    @Override
    public List<SmsNotificationLog> getAllSmsNotificationLogs() {
        return List.of();
    }

    @Override
    public List<SmsNotificationLog> getSmsNotificationLogsByRecipientPhoneNumber(String recipientPhoneNumber) {
        return List.of();
    }

    @Override
    public List<SmsNotificationLog> getSmsNotificationLogsByStatus(String status) {
        return List.of();
    }

    @Override
    public List<SmsNotificationLog> getSmsNotificationLogsByCreatedAtBetween(String start, String end) {
        return List.of();
    }

    @Override
    public List<SmsNotificationLog> getSmsNotificationLogsBySentAtBetween(String start, String end) {
        return List.of();
    }

    @Override
    public List<SmsNotificationLog> getSmsNotificationLogsByUpdatedAtBetween(String start, String end) {
        return List.of();
    }
}
