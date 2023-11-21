package com.ocm.admin.application.category.update;

import com.ocm.admin.domain.category.Category;
import com.ocm.admin.domain.category.CategoryID;

public record UpdateCategoryOutput(
        CategoryID id
) {
    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId());
    }
}
