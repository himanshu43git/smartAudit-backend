package com.audit.notification.repositories;

import com.audit.notification.model.EmailNotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailLogRepository extends JpaRepository<EmailNotificationLog, UUID> {

    Optional<EmailNotificationLog> findByRecipientEmailAndSubject(String recipientEmail, String subject);

    List<EmailNotificationLog> findByRecipientEmail(String recipientEmail);

    List<EmailNotificationLog> findByStatus(String status);

    List<EmailNotificationLog> findByCreatedAtBetween(Instant start, Instant end);

    List<EmailNotificationLog> findBySentAtBetween(Instant start, Instant end);

}
