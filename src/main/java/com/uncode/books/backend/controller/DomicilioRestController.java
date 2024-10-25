package com.uncode.books.backend.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uncode.books.backend.model.entity.Domicilio;
import com.uncode.books.backend.model.repository.DomicilioRepository;
import com.uncode.books.backend.service.DomicilioService;

@RestController
@RequestMapping("/domicilios")
public class DomicilioRestController extends BaseRestController<Domicilio, UUID, DomicilioService, DomicilioRepository> {

}
