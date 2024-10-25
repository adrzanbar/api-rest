package com.uncode.books.backend.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.uncode.books.backend.exception.ServiceException;
import com.uncode.books.backend.model.entity.Domicilio;
import com.uncode.books.backend.model.repository.DomicilioRepository;

@Service
public class DomicilioService extends BaseService<Domicilio, UUID> {

    @Autowired
    private DomicilioRepository repository;

    @Override
    protected DomicilioRepository getRepository() {
        return repository;
    }

    private void validate(Domicilio entity) throws ServiceException {
        if (repository.existsByCalleAndNumero(entity.getCalle(), entity.getNumero())) {
            throw new ServiceException("Ya existe un domicilio con la calle y número ingresados");
        }
    }

    @Override
    public Domicilio create(Domicilio entity) throws ServiceException {
        validate(entity);
        return super.create(entity);
    }

    @Override
    public Domicilio update(Domicilio entity) throws ServiceException, NotFoundException {
        validate(entity);
        return super.update(entity);
    }

}
