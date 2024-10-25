package com.uncode.books.backend.service;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uncode.books.backend.exception.ServiceException;
import com.uncode.books.backend.model.entity.Identifiable;

import jakarta.transaction.Transactional;

public abstract class BaseService<E extends Identifiable<ID>, ID> {

    protected abstract JpaRepository<E, ID> getRepository();

    @Transactional
    public E create(E entity) throws ServiceException {
        entity.setId(null);
        return getRepository().save(entity);
    }

    public Page<E> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    public E findById(ID id) throws NotFoundException {
        return getRepository().findById(id).orElseThrow(
                () -> new NotFoundException());
    }

    @Transactional
    public E update(E entity) throws ServiceException, NotFoundException {
        if (getRepository().existsById(entity.getId())) {
            return getRepository().save(entity);
        } else {
            throw new NotFoundException();
        }
    }

    @Transactional
    public void delete(ID id) throws NotFoundException {
        if (getRepository().existsById(id)) {
            getRepository().deleteById(id);
        } else {
            throw new NotFoundException();
        }
    }

}
