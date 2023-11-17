package com.ocm.admin.application.category.create;

import com.ocm.admin.domain.category.Category;
import com.ocm.admin.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

public class CreateCategoryUseCaseTest {

    // 1. Teste do caminho feliz
    // 2. Teste passando uma propriedade inválida (name)
    // 3. Teste criando uma categoria inativa
    // 4. Teste simulando um erro generico vindo do gateway

    @Test
    public void givenAValidaCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final CategoryGateway categoryGateway = mock(CategoryGateway.class);
        when(categoryGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var useCase = new DefaultCreateCategoryUseCase(categoryGateway);

        final var actualOutup = useCase.execute(aCommand);

        Assertions.assertNotNull(actualOutup);
        Assertions.assertNotNull(actualOutup.id());

        verify(categoryGateway, times(1))
                .create(argThat(aCategory ->
                        Objects.equals(expectedName, aCategory.getName())
                            && Objects.equals(expectedDescription, aCategory.getDescription())
                            && Objects.equals(expectedIsActive, aCategory.getIsActive())
                            && Objects.nonNull(aCategory.getId())
                            && Objects.nonNull(aCategory.getCreatedAt())
                            && Objects.nonNull(aCategory.getUpdatedAt())
                            && Objects.isNull(aCategory.getDeletedAt())
                ));
    }
}
