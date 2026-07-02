package com.audit.expense.services.impl;

import com.audit.expense.exceptions.CategoryAlreadyExistsException;
import com.audit.expense.exceptions.CategoryNotFoundException;
import com.audit.expense.exceptions.ExpenseNotFoundException;
import com.audit.expense.io.request.CategoryRequest;
import com.audit.expense.io.request.ExpenseRequest;
import com.audit.expense.io.response.CategoryResponse;
import com.audit.expense.io.response.ExpenseResponse;
import com.audit.expense.model.Category;
import com.audit.expense.model.Expense;
import com.audit.expense.model.ExpenseInfo;
import com.audit.expense.model.PaymentStatus;
import com.audit.expense.repositories.CategoryRepository;
import com.audit.expense.repositories.ExpenseRepository;
import com.audit.expense.services.ExpenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    // ==================== EXPENSE OPERATIONS ====================

    @Override
    public ExpenseResponse createExpense(UUID userId, ExpenseRequest request) {
        // Validate category exists and belongs to user
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + request.getCategoryId()));

        if (!category.getUserId().equals(userId)) {
            throw new CategoryNotFoundException(
                    "Category not found for user with id: " + request.getCategoryId());
        }

        // Build ExpenseInfo embedded object
        ExpenseInfo expenseInfo = new ExpenseInfo();
        expenseInfo.setAmount(request.getAmount());
        expenseInfo.setDate(request.getTransactionDate().toInstant(ZoneOffset.UTC));
        expenseInfo.setMerchantName(request.getMerchantName());
        expenseInfo.setDescription(request.getDescription());
        expenseInfo.setTitle(request.getTitle());
        if(request.getUnits() != null){
            expenseInfo.setUnits(request.getUnits());
        }else{
            expenseInfo.setUnits("1");
        }

        // Build Expense entity
        Expense expense = new Expense();
        expense.setUserId(userId);
        expense.setCategoryId(request.getCategoryId());
        expense.setInfo(expenseInfo);
        expense.setReceiptReferenceId(request.getReceiptReferenceId());
        expense.setPaymentStatus(request.getPaymentStatus() != null
                ? request.getPaymentStatus()
                : PaymentStatus.PENDING);

        Expense savedExpense = expenseRepository.save(expense);

        return mapToExpenseResponse(savedExpense, category.getName());
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse getExpenseById(UUID userId, UUID expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(
                        "Expense not found with id: " + expenseId));

        // Verify expense belongs to user
        if (!expense.getUserId().equals(userId)) {
            throw new ExpenseNotFoundException("Expense not found with id: " + expenseId);
        }

        String categoryName = getCategoryName(expense.getCategoryId());
        return mapToExpenseResponse(expense, categoryName);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByUserId(UUID userId) {
        List<Expense> expenses = expenseRepository.findByUserId(userId);

        return expenses.stream()
                .map(expense -> mapToExpenseResponse(expense, getCategoryName(expense.getCategoryId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByLastNDays(UUID userId, int days) {

        Instant end = Instant.now();
        Instant start = end.minus(days, ChronoUnit.DAYS);

        System.out.println("UserId = " + userId);

        System.out.println(userId.equals(UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479")));
        System.out.println("Days = " + days);

        System.out.println("Start: " + start);
        System.out.println("End: " + end);

        List<Expense> expenses =
                expenseRepository.findExpenses(userId, start, end);

        return expenses.stream()
                .map(expense -> mapToExpenseResponse(
                        expense,
                        getCategoryName(expense.getCategoryId())
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByCategory(UUID userId, UUID categoryId) {
        // Validate category exists and belongs to user
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + categoryId));

        if (!category.getUserId().equals(userId)) {
            throw new CategoryNotFoundException("Category not found for user with id: " + categoryId);
        }

        List<Expense> expenses = expenseRepository.findByUserIdAndCategoryId(userId, categoryId);

        return expenses.stream()
                .map(expense -> mapToExpenseResponse(expense, category.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ExpenseResponse> getExpensesByDateRange(UUID userId, LocalDateTime start, LocalDateTime end) {
        List<Expense> expenses = expenseRepository.findByUserIdAndInfo_DateBetween(userId, start, end);

        return expenses.stream()
                .map(expense -> mapToExpenseResponse(expense, getCategoryName(expense.getCategoryId())))
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponse updateExpense(UUID userId, UUID expenseId, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(
                        "Expense not found with id: " + expenseId));

        // Verify expense belongs to user
        if (!expense.getUserId().equals(userId)) {
            throw new ExpenseNotFoundException("Expense not found with id: " + expenseId);
        }

        // If category is being changed, validate new category
        String categoryName;
        if (request.getCategoryId() != null && !request.getCategoryId().equals(expense.getCategoryId())) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException(
                            "Category not found with id: " + request.getCategoryId()));

            if (!category.getUserId().equals(userId)) {
                throw new CategoryNotFoundException(
                        "Category not found for user with id: " + request.getCategoryId());
            }
            expense.setCategoryId(request.getCategoryId());
            categoryName = category.getName();
        } else {
            categoryName = getCategoryName(expense.getCategoryId());
        }

        // Update ExpenseInfo
        ExpenseInfo expenseInfo = expense.getInfo();
        if (expenseInfo == null) {
            expenseInfo = new ExpenseInfo();
        }

        if (request.getAmount() != null) {
            expenseInfo.setAmount(request.getAmount());
        }
        if (request.getTransactionDate() != null) {
            expenseInfo.setDate(request.getTransactionDate().toInstant(ZoneOffset.UTC));
        }
        if (request.getMerchantName() != null) {
            expenseInfo.setMerchantName(request.getMerchantName());
        }
        if (request.getDescription() != null) {
            expenseInfo.setDescription(request.getDescription());
        }
        if (request.getTitle() != null) {
            expenseInfo.setTitle(request.getTitle());
        }
        expense.setInfo(expenseInfo);

        // Update other fields
        if (request.getReceiptReferenceId() != null) {
            expense.setReceiptReferenceId(request.getReceiptReferenceId());
        }
        if (request.getPaymentStatus() != null) {
            expense.setPaymentStatus(request.getPaymentStatus());
        }

        Expense updatedExpense = expenseRepository.save(expense);

        return mapToExpenseResponse(updatedExpense, categoryName);
    }

    @Override
    public boolean deleteExpense(UUID userId, UUID expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException(
                        "Expense not found with id: " + expenseId));

        // Verify expense belongs to user
        if (!expense.getUserId().equals(userId)) {
            throw new ExpenseNotFoundException("Expense not found with id: " + expenseId);
        }

        expenseRepository.delete(expense);
        return true;
    }

    // ==================== CATEGORY OPERATIONS ====================

    @Override
    public CategoryResponse createCategory(UUID userId, CategoryRequest request) {
        // Check if category with same name already exists for user
        if (categoryRepository.existsByUserIdAndName(userId, request.getName())) {
            throw new CategoryAlreadyExistsException(
                    "Category with name '" + request.getName() + "' already exists");
        }

        Category category = new Category();
        category.setUserId(userId);
        category.setName(request.getName());
        category.setType(request.getType());
        category.setIconUrl(request.getIconUrl());

        Category savedCategory = categoryRepository.save(category);

        return mapToCategoryResponse(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(UUID userId, UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + categoryId));

        // Verify category belongs to user
        if (!category.getUserId().equals(userId)) {
            throw new CategoryNotFoundException("Category not found with id: " + categoryId);
        }

        return mapToCategoryResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategoriesByUserId(UUID userId) {
        List<Category> categories = categoryRepository.findByUserId(userId);

        return categories.stream()
                .map(this::mapToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse updateCategory(UUID userId, UUID categoryId, CategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + categoryId));

        // Verify category belongs to user
        if (!category.getUserId().equals(userId)) {
            throw new CategoryNotFoundException("Category not found with id: " + categoryId);
        }

        // Check if new name conflicts with existing category (if name is being changed)
        if (request.getName() != null && !request.getName().equals(category.getName())) {
            if (categoryRepository.existsByUserIdAndName(userId, request.getName())) {
                throw new CategoryAlreadyExistsException(
                        "Category with name '" + request.getName() + "' already exists");
            }
            category.setName(request.getName());
        }

        if (request.getType() != null) {
            category.setType(request.getType());
        }
        if (request.getIconUrl() != null) {
            category.setIconUrl(request.getIconUrl());
        }

        Category updatedCategory = categoryRepository.save(category);

        return mapToCategoryResponse(updatedCategory);
    }

    @Override
    public boolean deleteCategory(UUID userId, UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + categoryId));

        // Verify category belongs to user
        if (!category.getUserId().equals(userId)) {
            throw new CategoryNotFoundException("Category not found with id: " + categoryId);
        }

        categoryRepository.delete(category);
        return true;
    }

    // ==================== HELPER METHODS ====================

    private String getCategoryName(UUID categoryId) {
        if (categoryId == null) {
            return null;
        }
        return categoryRepository.findById(categoryId)
                .map(Category::getName)
                .orElse(null);
    }

    private ExpenseResponse mapToExpenseResponse(Expense expense, String categoryName) {
        ExpenseInfo info = expense.getInfo();

        return ExpenseResponse.builder()
                .id(expense.getId())
                .userId(expense.getUserId())
                .categoryId(expense.getCategoryId())
                .categoryName(categoryName)
                .amount(info != null ? info.getAmount() : null)
                .transactionDate(info != null ? info.getDate() : null)
                .merchantName(info != null ? info.getMerchantName() : null)
                .description(info != null ? info.getDescription() : null)
                .title(info != null ? info.getTitle() : null)
                .units(info != null ? info.getUnits() : null)
                .receiptReferenceId(expense.getReceiptReferenceId())
                .paymentStatus(expense.getPaymentStatus())
                .createdAt(expense.getCreatedAt())
                .updatedAt(expense.getUpdatedAt())
                .build();
    }

    private CategoryResponse mapToCategoryResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .userId(category.getUserId())
                .name(category.getName())
                .type(category.getType())
                .iconUrl(category.getIconUrl())
                .createdAt(category.getCreatedAt())
                .build();
    }

}
