package com.audit.expense.services;

import com.audit.expense.io.request.CategoryRequest;
import com.audit.expense.io.request.ExpenseRequest;
import com.audit.expense.io.response.CategoryResponse;
import com.audit.expense.io.response.ExpenseResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ExpenseService {

    ExpenseResponse createExpense(UUID userId, ExpenseRequest request);
    ExpenseResponse getExpenseById(UUID userId, UUID expenseId);
    List<ExpenseResponse> getExpensesByUserId(UUID userId);
    List<ExpenseResponse> getExpensesByLastNDays(UUID userId, int days);
    List<ExpenseResponse> getExpensesByCategory(UUID userId, UUID categoryId);
    List<ExpenseResponse> getExpensesByDateRange(UUID userId, LocalDateTime start, LocalDateTime end);
    ExpenseResponse updateExpense(UUID userId, UUID expenseId, ExpenseRequest request);
    boolean deleteExpense(UUID userId, UUID expenseId);

    // Category operations
    CategoryResponse createCategory(UUID userId, CategoryRequest request);
    CategoryResponse getCategoryById(UUID userId, UUID categoryId);
    List<CategoryResponse> getCategoriesByUserId(UUID userId);
    CategoryResponse updateCategory(UUID userId, UUID categoryId, CategoryRequest request);
    boolean deleteCategory(UUID userId, UUID categoryId);

}
