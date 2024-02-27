package br.com.widsl.rinhav2.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.widsl.rinhav2.util.CrebitCreator;

@ExtendWith(SpringExtension.class)
class TransacaoResponseTest {

    @Test
    void testeInstanciacaoEqualsTransacaoResponse() {

        TransacaoResponse transacaoRequest = new TransacaoResponse(1, 1);
        TransacaoResponse esperado = CrebitCreator.criarTransacaoResponse();

        assertEquals(esperado.limite(), transacaoRequest.limite());
        assertEquals(esperado.saldo(), transacaoRequest.saldo());

        boolean equals = esperado.equals(transacaoRequest);

        Assertions.assertTrue(equals);

    }

}
