package com.ocm.admin.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ocm.admin.ControllerTest;
import com.ocm.admin.application.category.create.CreateCategoryOutput;
import com.ocm.admin.application.category.create.CreateCategoryUseCase;
import com.ocm.admin.application.category.delete.DeleteCategoryUseCase;
import com.ocm.admin.application.category.retrieve.get.CategoryOutput;
import com.ocm.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.ocm.admin.application.category.retrieve.list.CategoryListOutput;
import com.ocm.admin.application.category.retrieve.list.ListCategoriesUseCase;
import com.ocm.admin.application.category.update.UpdateCategoryOutput;
import com.ocm.admin.application.category.update.UpdateCategoryUseCase;
import com.ocm.admin.domain.category.Category;
import com.ocm.admin.domain.category.CategoryID;
import com.ocm.admin.domain.exceptions.DomainException;
import com.ocm.admin.domain.exceptions.NotFoundException;
import com.ocm.admin.domain.pagination.Pagination;
import com.ocm.admin.domain.validation.Error;
import com.ocm.admin.domain.validation.handler.Notification;
import com.ocm.admin.infrastructure.category.models.CreateCategoryRequest;
import com.ocm.admin.infrastructure.category.models.UpdateCategoryRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void givenAValidaCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var anInput =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput.from("123")));

        // when
        final var request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/categories/123"))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo("123")));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var anInput =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        // when
        final var request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var anInput =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        // when
        final var request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(createCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception {
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category
                .newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(any()))
                .thenReturn(CategoryOutput.from(aCategory));

        // when
        final var request = get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(getCategoryByIdUseCase, times(1))
                .execute(eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception {
        // given
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";

        when(getCategoryByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        // when
        final var request = get("/categories/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    public void givenAValidaCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(new UpdateCategoryOutput(expectedId)));

        final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        // when
        final var request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenACommandWithInvalidId_whenCallsUpdateCategory_shouldReturnNotFound() throws Exception {
        // given
        final var expectedId = "not-found";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with ID not-found was not found";

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        // when
        final var request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedErrorMessage))));

        final var aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        // when
        final var request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldReturnNoContenta() throws Exception {
        // given
        final var expectedId = "123";

        doNothing()
                .when(deleteCategoryUseCase).execute(any());

        // when
        final var request = delete("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isNoContent());

        verify(deleteCategoryUseCase, times(1))
                .execute(eq(expectedId));
    }

    @Test
    public void givenValidParams_whenCallsListCategories_shouldReturnCategories() throws Exception {
        // given
        final var aCategory = Category.newCategory("Moveis", null, false);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(CategoryListOutput.from(aCategory));

        // when
        when(listCategoriesUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = get("/categories")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aCategory.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aCategory.getIsActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aCategory.getDeletedAt().toString())));

        verify(listCategoriesUseCase, times(1))
                .execute(argThat(query ->
                        Objects.equals(expectedPage, query.page())
                                && Objects.equals(expectedPerPage, query.perPage())
                                && Objects.equals(expectedDirection, query.direction())
                                && Objects.equals(expectedSort, query.sort())
                                && Objects.equals(expectedTerms, query.terms())
                ));
    }
}
