package com.ocm.admin.application.category.retrieve.get;

import com.ocm.admin.domain.category.CategoryGateway;
import com.ocm.admin.domain.category.CategoryID;
import com.ocm.admin.domain.exceptions.DomainException;
import com.ocm.admin.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(String anIn) {
        final var anCategoryId = CategoryID.from(anIn);
        return this.categoryGateway.findById(anCategoryId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(anCategoryId));
    }

    private static Supplier<DomainException> notFound(CategoryID anId) {
        return () -> DomainException.with(
                new Error("Category with ID %s was not-found".formatted(anId.getValue())));
    }
}
