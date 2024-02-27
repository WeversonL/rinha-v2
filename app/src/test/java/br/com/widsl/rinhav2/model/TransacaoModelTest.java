package br.com.widsl.rinhav2.model;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.widsl.rinhav2.util.CrebitCreator;

@ExtendWith(SpringExtension.class)
class TransacaoModelTest {

    @Test
    void testeInstanciacaoEqualsTransacaoModel() {

        OffsetDateTime horario = OffsetDateTime.of(CrebitCreator.HORARIO, ZoneOffset.UTC);

        TransacaoModel transacaoModel = new TransacaoModel(1, 1, horario, "desc", "c");
        TransacaoModel esperado = CrebitCreator.criarTransacaoModel();

        Assertions.assertEquals(esperado.clienteId(), transacaoModel.clienteId());
        Assertions.assertEquals(esperado.valor(), transacaoModel.valor());
        Assertions.assertEquals(esperado.descricao(), transacaoModel.descricao());
        Assertions.assertEquals(esperado.tipo(), transacaoModel.tipo());
        Assertions.assertEquals(esperado.realizadaEm(), transacaoModel.realizadaEm());

        boolean equals = esperado.equals(transacaoModel);

        Assertions.assertTrue(equals);

    }

}
