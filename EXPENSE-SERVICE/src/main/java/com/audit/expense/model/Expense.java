package com.audit.expense.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Embedded;
import jakarta.persistence.EnumType;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "expenses", indexes = {
        @Index(name = "idx_user_date", columnList = "user_id, transaction_date"),
        @Index(name = "idx_user_category", columnList = "user_id, category_id")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "VARCHAR(36)") // Or BINARY(16) for efficiency
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "user_id", nullable = false, columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID userId;

    @Column(name = "category_id", columnDefinition = "VARCHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID categoryId;

    @Embedded
    private ExpenseInfo info;

    @Column(name = "receipt_reference_id")
    private String receiptReferenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", columnDefinition = "ENUM('PENDING', 'PAID', 'PARTIAL')")
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}