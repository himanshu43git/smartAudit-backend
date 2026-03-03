package com.audit.expense.repositories;

import com.audit.expense.model.Category;
import com.audit.expense.model.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    // Fetch all categories belonging to a user
    List<Category> findByUserId(UUID userId);

    // Fetch all categories belonging to a user ordered by name
    List<Category> findByUserIdOrderByNameAsc(UUID userId);

    // Fetch categories by user and type (e.g., "Show me all INCOME categories")
    List<Category> findByUserIdAndType(UUID userId, CategoryType type);

    // Check if a category name already exists for this user (to prevent duplicates)
    boolean existsByUserIdAndName(UUID userId, String name);

    // Find specific category by name for a user
    Optional<Category> findByUserIdAndName(UUID userId, String name);

    // Count categories for a user
    long countByUserId(UUID userId);

    // Delete all categories for a user (useful when user deletes account)
    void deleteAllByUserId(UUID userId);

    // Check if category exists for user
    boolean existsByIdAndUserId(UUID id, UUID userId);

}
