package com.ocm.admin.application.category.create;

import com.ocm.admin.application.UseCase;
import com.ocm.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}
