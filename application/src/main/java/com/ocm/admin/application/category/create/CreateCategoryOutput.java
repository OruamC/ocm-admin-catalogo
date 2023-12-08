package com.ocm.admin.application.category.create;

import com.ocm.admin.domain.category.Category;
import com.ocm.admin.domain.category.CategoryID;

public record CreateCategoryOutput(
        CategoryID id
) {
    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId());
    }
}
