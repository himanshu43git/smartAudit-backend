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
@Table(name = "push_notification_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PushNotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "PushNotificationLog_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "device_token", updatable = false, nullable = false)
    private String deviceToken;

    @Column(name = "title", updatable = false, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(name = "status", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @CreationTimestamp
    private Instant createdAt;

    @CreationTimestamp
    private Instant sentAt;



}
