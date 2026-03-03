package com.audit.notification.repositories;

import com.audit.notification.model.SmsNotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SmsLogRepository extends JpaRepository<SmsNotificationLog, UUID> {

    Optional<SmsNotificationLog> findByPhoneNumberAndMessage(String recipientPhoneNumber, String message);

    Optional<SmsNotificationLog> findByPhoneNumber(String recipientPhoneNumber);

    List<SmsNotificationLog> findByStatus(String status);

    List<SmsNotificationLog> findByCreatedAtBetween(Instant start, Instant end);

    List<SmsNotificationLog> findBySentAtBetween(Instant start, Instant end);

}
