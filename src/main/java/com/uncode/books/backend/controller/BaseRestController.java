package com.uncode.books.backend.controller;

import java.util.Map;
import java.util.HashMap;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.uncode.books.backend.entity.Identifiable;
import com.uncode.books.backend.exception.NotFoundException;
import com.uncode.books.backend.exception.ServiceException;
import com.uncode.books.backend.service.BaseService;

import jakarta.validation.Valid;

public abstract class BaseRestController<E extends Identifiable<ID>, ID> {

    protected abstract BaseService<E, ID> getService();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public E post(@Valid @RequestBody E entity) throws ServiceException {
        return getService().create(entity);
    }

    @GetMapping
    public Page<E> get(Pageable pageable) {
        return getService().read(pageable);
    }

    @GetMapping("/{id}")
    public E get(@PathVariable ID id) {
        return getService().read(id);
    }

    @PutMapping("/{id}")
    public E put(@PathVariable ID id, @Valid @RequestBody E entity) {
        entity.setId(id);
        return getService().update(entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable ID id) {
        getService().delete(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var errors = new HashMap<String, String>();
        e.getBindingResult().getFieldErrors().forEach(
                (FieldError fe) -> errors.put(fe.getField(), fe.getDefaultMessage()));
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Los datos ingresados no son válidos");
        response.put("fieldErrors", errors);
        return response;
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<Map<String, Object>> handleServiceException(ServiceException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Error de servicio");
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "Not Found");
        response.put("message", "The requested resource was not found.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e) {
        return e.getMessage();
    }

}