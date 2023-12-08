package com.ocm.admin.infrastructure.category;

import com.ocm.admin.domain.category.Category;
import com.ocm.admin.domain.category.CategoryGateway;
import com.ocm.admin.domain.category.CategoryID;
import com.ocm.admin.domain.category.CategorySearchQuery;
import com.ocm.admin.domain.pagination.Pagination;
import com.ocm.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.ocm.admin.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(
            final CategoryRepository repository
    ) {
        this.repository = repository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(CategoryID anId) {

    }

    @Override
    public Optional<Category> findById(CategoryID anId) {
        return Optional.empty();
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return null;
    }

    private Category save(Category aCategory) {
        return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }
}
