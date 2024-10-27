package com.uncode.books.backend.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uncode.books.backend.entity.Domicilio;
import com.uncode.books.backend.service.BaseService;
import com.uncode.books.backend.service.DomicilioService;

@RestController
@RequestMapping("/domicilios")
public class DomicilioRestController extends BaseRestController<Domicilio, UUID> {

    @Autowired
    private DomicilioService service;

    @Override
    protected BaseService<Domicilio, UUID> getService() {
        return service;
    }
}
