package com.audit.expense.controller;

import com.audit.expense.io.request.CategoryRequest;
import com.audit.expense.io.response.CategoryResponse;
import com.audit.expense.services.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final ExpenseService expenseService;

    public CategoryController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    /**
     * Create a new category for a user
     * POST /category/{userId}
     */
    @PostMapping("/{userId}")
    public ResponseEntity<CategoryResponse> createCategory(
            @PathVariable("userId") UUID userId,
            @RequestBody @Valid CategoryRequest request) {

        CategoryResponse response = expenseService.createCategory(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Get all categories for a user
     * GET /category/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByUserId(
            @PathVariable("userId") UUID userId) {

        List<CategoryResponse> response = expenseService.getCategoriesByUserId(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Get a specific category by ID
     * GET /category/{userId}/{categoryId}
     */
    @GetMapping("/{userId}/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable("userId") UUID userId,
            @PathVariable("categoryId") UUID categoryId) {

        CategoryResponse response = expenseService.getCategoryById(userId, categoryId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Update a category
     * PUT /category/{userId}/{categoryId}
     */
    @PutMapping("/{userId}/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable("userId") UUID userId,
            @PathVariable("categoryId") UUID categoryId,
            @RequestBody @Valid CategoryRequest request) {

        CategoryResponse response = expenseService.updateCategory(userId, categoryId, request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    /**
     * Delete a category
     * DELETE /category/{userId}/{categoryId}
     */
    @DeleteMapping("/{userId}/{categoryId}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable("userId") UUID userId,
            @PathVariable("categoryId") UUID categoryId) {

        expenseService.deleteCategory(userId, categoryId);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

}
