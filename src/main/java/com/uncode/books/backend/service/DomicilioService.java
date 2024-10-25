package com.uncode.books.backend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.uncode.books.backend.model.entity.Domicilio;
import com.uncode.books.backend.model.repository.DomicilioRepository;

@Service
public class DomicilioService extends BaseService<Domicilio, UUID, DomicilioRepository> {
}
