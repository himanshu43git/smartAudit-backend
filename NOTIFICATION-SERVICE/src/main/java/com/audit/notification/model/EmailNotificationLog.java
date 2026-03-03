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
@Table(name = "email_notification_log")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "EmailNotificationLog_id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "recipient_email", updatable = false, nullable = false)
    private String recipientEmail;

    @Column(name = "subject", updatable = false, nullable = false)
    private String subject;

    @Column(name = "body", updatable = false, nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "status", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;

    @CreationTimestamp
    private Instant createdAt;

    @CreationTimestamp
    private Instant sentAt;

}
