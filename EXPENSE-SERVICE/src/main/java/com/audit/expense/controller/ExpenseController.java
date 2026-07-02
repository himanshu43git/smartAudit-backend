package com.audit.expense.controller;

import com.audit.expense.io.request.ExpenseRequest;
import com.audit.expense.io.response.ExpenseResponse;
import com.audit.expense.services.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/expense")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Create a new expense for a user
     * POST /expense/{userId}
     */
    @PostMapping("/{userId}")
    public ResponseEntity<ExpenseResponse> createExpense(
            @PathVariable("userId") UUID userId,
            @RequestBody @Valid ExpenseRequest request) {

        ExpenseResponse response = expenseService.createExpense(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Get a specific expense by ID
     * GET /expense/{userId}/{expenseId}
     */
    @GetMapping("/{userId}/{expenseId}")
    public ResponseEntity<ExpenseResponse> getExpenseById(
            @PathVariable("userId") UUID userId,
            @PathVariable("expenseId") UUID expenseId) {

        ExpenseResponse response = expenseService.getExpenseById(userId, expenseId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Get all expenses for a user
     * GET /expense/{userId}
     */
//    @GetMapping("/{userId}")
//    public ResponseEntity<List<ExpenseResponse>> getAllExpensesByUserId(
//            @PathVariable("userId") UUID userId) {
//
//        List<ExpenseResponse> response = expenseService.getExpensesByUserId(userId);
//
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(response);
//    }

    @GetMapping("/getExpenses/{userId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByLastNDays(
            @PathVariable("userId") UUID userId,
            @RequestParam(value = "days", defaultValue = "30") int days){

        System.out.println("Reached here");

        List<ExpenseResponse> response = expenseService.getExpensesByLastNDays(userId, days);

        System.out.println("Expenses found = " + response.size());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Get expenses by category
     * GET /expense/{userId}/category/{categoryId}
     */
    @GetMapping("/{userId}/category/{categoryId}")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByCategory(
            @PathVariable("userId") UUID userId,
            @PathVariable("categoryId") UUID categoryId) {

        List<ExpenseResponse> response = expenseService.getExpensesByCategory(userId, categoryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Get expenses by date range
     * GET /expense/{userId}/date-range?start=2024-01-01T00:00:00&end=2024-12-31T23:59:59
     */
    @GetMapping("/{userId}/date-range")
    public ResponseEntity<List<ExpenseResponse>> getExpensesByDateRange(
            @PathVariable("userId") UUID userId,
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<ExpenseResponse> response = expenseService.getExpensesByDateRange(userId, start, end);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Update an expense
     * PUT /expense/{userId}/{expenseId}
     */
    @PutMapping("/{userId}/{expenseId}")
    public ResponseEntity<ExpenseResponse> updateExpense(
            @PathVariable("userId") UUID userId,
            @PathVariable("expenseId") UUID expenseId,
            @RequestBody @Valid ExpenseRequest request) {

        ExpenseResponse response = expenseService.updateExpense(userId, expenseId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Delete an expense
     * DELETE /expense/{userId}/{expenseId}
     */
    @DeleteMapping("/{userId}/{expenseId}")
    public ResponseEntity<Void> deleteExpense(
            @PathVariable("userId") UUID userId,
            @PathVariable("expenseId") UUID expenseId) {

        expenseService.deleteExpense(userId, expenseId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
