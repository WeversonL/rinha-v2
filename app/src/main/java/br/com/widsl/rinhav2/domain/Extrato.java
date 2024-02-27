package br.com.widsl.rinhav2.domain;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Extrato(Saldo saldo, @JsonProperty("ultimas_transacoes") List<Transacao> ultimasTransacoes) {

    public record Saldo(Integer total, Integer limite, @JsonProperty("data_extrato") LocalDateTime dataExtrato) {
    }

    public record Transacao(Integer valor, String tipo, String descricao,
            @JsonProperty("realizada_em") ZonedDateTime realizadaEm) {
    }

}
