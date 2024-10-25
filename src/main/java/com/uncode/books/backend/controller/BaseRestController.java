package com.uncode.books.backend.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.uncode.books.backend.model.entity.Identifiable;
import com.uncode.books.backend.service.BaseService;
import com.uncode.books.backend.service.FieldValidationException;
import com.uncode.books.backend.service.ServiceException;
import com.uncode.books.backend.service.UniqueConstraintException;

public abstract class BaseRestController<E extends Identifiable<ID>, ID, S extends BaseService<E, ID, R>, R extends JpaRepository<E, ID>> {

    @Autowired
    protected S service;

    @GetMapping
    public ResponseEntity<?> get(Pageable pageable) {
        try {
            Page<E> page = service.read(pageable);
            return ResponseEntity.ok(page);
        } catch (ServiceException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable ID id) {
        try {
            var optional = service.read(id);
            if (optional.isPresent()) {
                return ResponseEntity.ok(optional.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ServiceException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody E entity) {
        try {
            // Create the entity and get the newly created instance
            E createdEntity = service.create(entity);

            // Build the URI for the created resource
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}") // Assuming your resource has an ID
                    .buildAndExpand(createdEntity.getId()) // Replace with the ID of the created entity
                    .toUri();

            // Return a response with the URI in the Location header
            return ResponseEntity.created(location).body(createdEntity);
        } catch (FieldValidationException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("fields", e.getErrors());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (DataIntegrityViolationException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            var uce = new UniqueConstraintException(e);
            errorResponse.put("error", uce.getMessage());
            errorResponse.put("fields", uce.getErrors());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (ServiceException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ocurrió un error inesperado");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> put(@PathVariable ID id, @RequestBody E entity) {
        try {
            entity.setId(id);
            return ResponseEntity.ok(service.update(entity));
        } catch (FieldValidationException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            errorResponse.put("fields", e.getErrors());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (DataIntegrityViolationException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            var uce = new UniqueConstraintException(e);
            errorResponse.put("error", uce.getMessage());
            errorResponse.put("fields", uce.getErrors());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (ServiceException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ocurrió un error inesperado");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable ID id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ServiceException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
