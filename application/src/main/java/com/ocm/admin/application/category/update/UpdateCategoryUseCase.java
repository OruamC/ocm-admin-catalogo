package com.ocm.admin.application.category.update;

import com.ocm.admin.application.UseCase;
import com.ocm.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
