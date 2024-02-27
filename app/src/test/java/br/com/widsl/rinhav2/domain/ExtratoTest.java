package br.com.widsl.rinhav2.domain;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.widsl.rinhav2.util.CrebitCreator;

class ExtratoTest {

    @Test
    void testeInstanciacaoEqualsExtrato() {

        Extrato extrato = new Extrato(new Extrato.Saldo(1, 1, CrebitCreator.HORARIO),
                List.of(new Extrato.Transacao(1, "c", "desc", ZonedDateTime
                        .of(CrebitCreator.HORARIO, ZoneOffset.UTC))));

        Extrato esperado = CrebitCreator.criarExtrato();

        Assertions.assertEquals(esperado.saldo(), extrato.saldo());
        Assertions.assertEquals(esperado.ultimasTransacoes(), extrato.ultimasTransacoes());

        boolean equals = esperado.equals(extrato);

        Assertions.assertTrue(equals);

    }

}
