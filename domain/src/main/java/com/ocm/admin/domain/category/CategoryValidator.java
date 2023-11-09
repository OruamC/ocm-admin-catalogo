package com.ocm.admin.domain.category;

import com.ocm.admin.domain.validation.Error;
import com.ocm.admin.domain.validation.ValidationHandler;
import com.ocm.admin.domain.validation.Validator;

public class CategoryValidator extends Validator {

    private final Category category;

    protected CategoryValidator(final Category category, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = category;
    }

    @Override
    public void validate() {
        if(this.category.getName() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }
}
