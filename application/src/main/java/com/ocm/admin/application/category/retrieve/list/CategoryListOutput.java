package com.ocm.admin.application.category.retrieve.list;

import com.ocm.admin.domain.category.Category;
import com.ocm.admin.domain.category.CategoryID;

import java.time.Instant;

public record CategoryListOutput(
        CategoryID id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt
) {

    public static CategoryListOutput from(Category aCategory) {
        return new CategoryListOutput(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.getIsActive(),
                aCategory.getCreatedAt(),
                aCategory.getDeletedAt()
        );
    }
}
