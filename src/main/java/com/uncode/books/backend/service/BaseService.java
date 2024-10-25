package com.uncode.books.backend.service;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uncode.books.backend.model.entity.Identifiable;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

public abstract class BaseService<E extends Identifiable<ID>, ID, R extends JpaRepository<E, ID>> {

    @Autowired
    protected R repository;

    @Autowired
    protected Validator validator;

    private static final String ERROR_MESSAGE = "Ocurrió un error";
    private static final String UNIQUE_CONSTRAINT_MESSAGE = "Unique constraint violation";

    private Map<String, String> convertViolations(Set<ConstraintViolation<E>> violations) {
        Map<String, String> errors = new HashMap<>();
        for (ConstraintViolation<E> violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }

    @Transactional
    public E create(E entity) throws ServiceException {
        entity.setId(null);
        var violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new FieldValidationException(convertViolations(violations));
        }
        try {
            return repository.save(entity);
        } catch (Exception e) {
            throw new ServiceException(ERROR_MESSAGE);
        }
    }

    public Page<E> read(Pageable pageable) throws ServiceException {
        try {
            return repository.findAll(pageable);
        } catch (Exception e) {
            throw new ServiceException(ERROR_MESSAGE);
        }
    }

    public Optional<E> read(ID id) throws ServiceException {
        try {
            return repository.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(ERROR_MESSAGE);
        }
    }

    @Transactional
    public E update(E entity) throws ServiceException {
        var violations = validator.validate(entity);
        if (!violations.isEmpty()) {
            throw new FieldValidationException(convertViolations(violations));
        }
        try {
            return repository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException(UNIQUE_CONSTRAINT_MESSAGE);
        } catch (Exception e) {
            throw new ServiceException(ERROR_MESSAGE);
        }
    }

    @Transactional
    public void delete(ID id) throws ServiceException {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            throw new ServiceException(ERROR_MESSAGE);
        }
    }

}
