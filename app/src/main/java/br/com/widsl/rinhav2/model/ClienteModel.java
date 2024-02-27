package br.com.widsl.rinhav2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("clientes")
public record ClienteModel(@Id Integer id, Integer limite, Integer saldo) {
}