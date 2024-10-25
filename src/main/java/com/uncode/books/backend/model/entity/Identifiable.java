package com.uncode.books.backend.model.entity;

public interface Identifiable<T> {
    T getId();

    void setId(T id);
}
