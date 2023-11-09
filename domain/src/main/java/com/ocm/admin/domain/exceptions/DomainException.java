package com.ocm.admin.domain.exceptions;

import com.ocm.admin.domain.validation.Error;

import java.util.List;

public class DomainException extends RuntimeException{

    private final List<Error> errors;

    public DomainException(final List<Error> anErrors) {
        super("", null, true, false);
        this.errors = anErrors;
    }

    public static DomainException with(final List<Error> anErros) {
        return new DomainException(anErros);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
