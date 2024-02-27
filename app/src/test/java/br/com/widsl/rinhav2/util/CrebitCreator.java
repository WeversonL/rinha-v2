package br.com.widsl.rinhav2.util;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import br.com.widsl.rinhav2.domain.Extrato;
import br.com.widsl.rinhav2.domain.Extrato.Saldo;
import br.com.widsl.rinhav2.domain.Extrato.Transacao;
import br.com.widsl.rinhav2.domain.TransacaoRequest;
import br.com.widsl.rinhav2.domain.TransacaoResponse;
import br.com.widsl.rinhav2.model.ClienteModel;
import br.com.widsl.rinhav2.model.TransacaoModel;

public class CrebitCreator {

    public static final LocalDateTime HORARIO = LocalDateTime.parse("2024-02-26T12:30:45");

    private CrebitCreator() {
    }

    public static TransacaoRequest criarTransacaoRequest() {
        return new TransacaoRequest(1, "c", "desc");
    }

    public static TransacaoResponse criarTransacaoResponse() {
        return new TransacaoResponse(1, 1);
    }

    public static Extrato criarExtrato() {
        return new Extrato(new Saldo(1, 1, HORARIO),
                List.of(new Transacao(1, "c", "desc", ZonedDateTime
                        .of(HORARIO, ZoneOffset.UTC))));
    }

    public static ClienteModel criarClienteModel() {
        return new ClienteModel(1, 1, 1);
    }

    public static TransacaoModel criarTransacaoModel() {
        return new TransacaoModel(1, 1, OffsetDateTime.of(HORARIO, ZoneOffset.UTC),
                "desc", "c");
    }

}
