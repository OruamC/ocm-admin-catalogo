package com.ocm.admin.application.category.retrieve.list;

import com.ocm.admin.application.UseCase;
import com.ocm.admin.domain.category.CategorySearchQuery;
import com.ocm.admin.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
