package com.uncode.books.backend.entity;

public interface Identifiable<T> {

    T getId();

    void setId(T id);

}
