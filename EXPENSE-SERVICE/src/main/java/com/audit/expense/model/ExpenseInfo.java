package com.audit.expense.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseInfo {

    // Maps "amount" in Java to "original_amount" in MySQL
    @Column(name = "original_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    // Maps "date" in Java to "transaction_date" in MySQL
    @Column(name = "transaction_date", nullable = false)
    private Instant date;

    @Column(name = "merchant_name", length = 100)
    private String merchantName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "title")
    private String title;

    @Column(name = "no_of_units")
    private String units;
}