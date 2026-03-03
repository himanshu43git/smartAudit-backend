package com.audit.notification.repositories;

import com.audit.notification.model.PushNotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PushLogRepository extends JpaRepository<PushNotificationLog, UUID> {

    Optional<PushNotificationLog> findByDeviceTokenAndMessage(String deviceToken, String message);

    Optional<PushNotificationLog> findByDeviceToken(String deviceToken);

    List<PushNotificationLog> findByStatus(String status);

    List<PushNotificationLog> findByCreatedAtBetween(Instant start, Instant end);

    List<PushNotificationLog> findBySentAtBetween(Instant start, Instant end);

}
