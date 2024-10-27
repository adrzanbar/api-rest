package com.uncode.books.backend.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uncode.books.backend.entity.Identifiable;
import com.uncode.books.backend.exception.NotFoundException;
import com.uncode.books.backend.exception.ServiceException;

import jakarta.transaction.Transactional;

public abstract class BaseService<E extends Identifiable<ID>, ID> {

    protected abstract JpaRepository<E, ID> getRepository();

    protected abstract void validate(E entity) throws ServiceException;

    @Transactional
    public E create(E entity) {
        validate(entity);
        entity.setId(null);
        return getRepository().save(entity);
    }

    public Page<E> read(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    public E read(ID id) {
        return getRepository().findById(id).orElseThrow(
                () -> new NotFoundException("No se encontró el registro con el identificador " + id));
    }

    @Transactional
    public E update(E entity) throws ServiceException, NotFoundException {
        if (getRepository().existsById(entity.getId())) {
            validate(entity);
            return getRepository().save(entity);
        } else {
            throw new NotFoundException("No se encontró el registro con el identificador " + entity.getId());
        }
    }

    @Transactional
    public void delete(ID id) throws NotFoundException {
        if (getRepository().existsById(id)) {
            getRepository().deleteById(id);
        } else {
            throw new NotFoundException("No se encontró el registro con el identificador " + id);
        }
    }

}
