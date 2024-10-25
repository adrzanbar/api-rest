package com.uncode.books.backend.model.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uncode.books.backend.model.entity.Domicilio;

@Repository
public interface DomicilioRepository extends JpaRepository<Domicilio, UUID> {

    boolean existsByCalleAndNumero(String calle, Integer numero);

}
