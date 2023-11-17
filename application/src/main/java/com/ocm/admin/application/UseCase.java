package com.ocm.admin.application;

import com.ocm.admin.domain.category.Category;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);
}