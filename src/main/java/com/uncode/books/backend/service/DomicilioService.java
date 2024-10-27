package com.uncode.books.backend.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uncode.books.backend.entity.Domicilio;
import com.uncode.books.backend.exception.ServiceException;
import com.uncode.books.backend.repository.DomicilioRepository;

@Service
public class DomicilioService extends BaseService<Domicilio, UUID> {

    @Autowired
    private DomicilioRepository repository;

    @Override
    protected DomicilioRepository getRepository() {
        return repository;
    }

    protected void validate(Domicilio entity) throws ServiceException {
        if (repository.existsByCalleAndNumero(entity.getCalle(), entity.getNumero())) {
            throw new ServiceException("Ya existe un domicilio con la calle y número ingresados");
        }
    }

}
