package com.uncode.books.backend.model.entity;

import java.util.UUID;

import org.hibernate.annotations.SoftDelete;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.envers.Audited;

@Entity
@SoftDelete
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"calle", "numero"}))
@Data
@Audited
public class Domicilio implements Identifiable<UUID> {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank(message = "La calle no puede estar vacía")
    private String calle;
    @NotNull(message = "El número no puede estar vacío")
    @Positive(message = "El número debe ser mayor a 0")
    private Integer numero;
}
