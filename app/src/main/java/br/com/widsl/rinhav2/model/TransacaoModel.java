package br.com.widsl.rinhav2.model;

import java.time.OffsetDateTime;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("transacoes")
public record TransacaoModel(@Column("cliente_id") Integer clienteId, Integer valor,
                @Column("realizada_em") OffsetDateTime realizadaEm, String descricao, String tipo) {
}
