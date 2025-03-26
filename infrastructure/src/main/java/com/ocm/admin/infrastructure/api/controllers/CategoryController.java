package com.ocm.admin.infrastructure.api.controllers;

import com.ocm.admin.application.category.create.CreateCategoryCommand;
import com.ocm.admin.application.category.create.CreateCategoryOutput;
import com.ocm.admin.application.category.create.CreateCategoryUseCase;
import com.ocm.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.ocm.admin.application.category.update.UpdateCategoryCommand;
import com.ocm.admin.application.category.update.UpdateCategoryOutput;
import com.ocm.admin.application.category.update.UpdateCategoryUseCase;
import com.ocm.admin.domain.pagination.Pagination;
import com.ocm.admin.domain.validation.handler.Notification;
import com.ocm.admin.infrastructure.api.CategoryAPI;
import com.ocm.admin.infrastructure.category.models.CategoryApiOutput;
import com.ocm.admin.infrastructure.category.models.CreateCategoryApiInput;
import com.ocm.admin.infrastructure.category.models.UpdateCategoryApiInput;
import com.ocm.admin.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            UpdateCategoryUseCase updateCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = updateCategoryUseCase;
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }

    @Override
    public CategoryApiOutput getById(final String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> udateById(final String id, final UpdateCategoryApiInput input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }
}
