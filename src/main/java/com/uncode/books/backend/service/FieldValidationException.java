package com.uncode.books.backend.service;

import java.util.Map;
import java.util.HashMap;


public class FieldValidationException extends ServiceException {

    private final Map<String, String> errors = new HashMap<>();

    public FieldValidationException(Map<String, String> errors) {
        super("Field validation error");
        this.errors.putAll(errors);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

}
