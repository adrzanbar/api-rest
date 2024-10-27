package com.uncode.books.backend.controller;

import java.util.List;
import java.util.HashMap;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    public E get(@PathVariable ID id) throws NotFoundException {
        return getService().read(id);
    }

    @PutMapping("/{id}")
    public E put(@PathVariable ID id, @Valid @RequestBody E entity)
            throws ServiceException, NotFoundException {
        entity.setId(id);
        return getService().update(entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable ID id) throws NotFoundException {
        getService().delete(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var map = new HashMap<String, Object>();
        map.put("error", "Los datos ingresados no son válidos");
        e.getBindingResult().getFieldErrors().stream().forEach(
                (FieldError fe) -> map.put(fe.getField(), fe.getDefaultMessage()));
        return List.of(map.toString());
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public String handleServiceException(ServiceException e) {
        return e.getMessage();
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFoundException(NotFoundException e) {
        return "No se encontró el recurso solicitado";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleException(Exception e) {
        return "Ocurrió un error inesperado";

        // Do not expose the exception details to the client in a production environment
        // e.printStackTrace();
        // return e.getClass().getSimpleName();
    }

}
