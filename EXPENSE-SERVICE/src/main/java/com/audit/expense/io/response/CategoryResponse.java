package com.audit.expense.io.response;

import com.audit.expense.model.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {

    private UUID id;

    private UUID userId;

    private String name;

    private CategoryType type;

    private String iconUrl;

    private Instant createdAt;

}
