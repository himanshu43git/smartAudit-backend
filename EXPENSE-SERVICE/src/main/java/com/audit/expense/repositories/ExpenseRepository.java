package com.audit.expense.repositories;

import com.audit.expense.model.Expense;
import com.audit.expense.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {

    // Fetch all expenses for a specific user
    List<Expense> findByUserId(UUID userId);

    // Fetch all expenses for a specific user ordered by date descending
    List<Expense> findByUserIdOrderByInfo_DateDesc(UUID userId);

    // Fetch expenses for a user within a specific category
    List<Expense> findByUserIdAndCategoryId(UUID userId, UUID categoryId);

    // Fetch expenses for a user between two dates (e.g., for Monthly Reports)
    // Note: We use 'Info_Date' to traverse the embedded 'info' object
    List<Expense> findByUserIdAndInfo_DateBetween(UUID userId, LocalDateTime startDate, LocalDateTime endDate);

    // Search by User and Keyword in Title (using Embedded field)
    List<Expense> findByUserIdAndInfo_TitleContainingIgnoreCase(UUID userId, String keyword);

    // Fetch expenses by payment status
    List<Expense> findByUserIdAndPaymentStatus(UUID userId, PaymentStatus paymentStatus);

    // Sum of expenses for a user within a date range
    @Query("SELECT COALESCE(SUM(e.info.amount), 0) FROM Expense e WHERE e.userId = :userId AND e.info.date BETWEEN :startDate AND :endDate")
    BigDecimal sumExpensesByUserAndDateRange(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Sum of expenses for a user by category
    @Query("SELECT COALESCE(SUM(e.info.amount), 0) FROM Expense e WHERE e.userId = :userId AND e.categoryId = :categoryId")
    BigDecimal sumExpensesByUserAndCategory(
            @Param("userId") UUID userId,
            @Param("categoryId") UUID categoryId);

    // Count expenses for a user
    long countByUserId(UUID userId);

    // Delete all expenses for a user (useful when user deletes account)
    void deleteAllByUserId(UUID userId);

    // Check if expense exists for user
    boolean existsByIdAndUserId(UUID id, UUID userId);

    List<Expense> findByUserIdAndInfoDateBetween(UUID userId, Instant start, Instant end);

}
