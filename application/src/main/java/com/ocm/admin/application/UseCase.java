package com.ocm.admin.application;

import com.ocm.admin.domain.category.Category;

public class UseCase {

    public Category execute() {
        return Category.newCategory("Teste", "Teste", true);
    }
}