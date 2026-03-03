package com.audit.notification.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "sms_notification_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsNotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "SmsNotificationLog_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "phone_number", updatable = false, nullable = false)
    private String phoneNumber;

    @Column(name = "message", updatable = false, nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "status", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @CreationTimestamp
    private Instant createdAt;

    @CreationTimestamp
    private Instant sentAt;

}
