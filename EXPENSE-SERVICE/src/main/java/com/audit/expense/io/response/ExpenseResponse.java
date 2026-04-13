package com.audit.expense.io.response;

import com.audit.expense.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseResponse {

    private UUID id;

    private UUID userId;

    private UUID categoryId;

    private String categoryName;

    private BigDecimal amount;

    private Instant transactionDate;

    private String merchantName;

    private String description;

    private String title;

    private String receiptReferenceId;

    private PaymentStatus paymentStatus;

    private Instant createdAt;

    private Instant updatedAt;

}
