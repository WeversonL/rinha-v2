package br.com.widsl.rinhav2.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.widsl.rinhav2.util.CrebitCreator;

@ExtendWith(SpringExtension.class)
class ClienteModelTest {

    @Test
    void testeInstanciacaoEqualsClienteModel() {
        ClienteModel clienteModel = new ClienteModel(1, 1, 1);
        ClienteModel esperado = CrebitCreator.criarClienteModel();

        Assertions.assertEquals(esperado.saldo(), clienteModel.saldo());
        Assertions.assertEquals(esperado.limite(), clienteModel.limite());
        Assertions.assertEquals(esperado.id(), clienteModel.id());

        boolean equals = esperado.equals(clienteModel);

        Assertions.assertTrue(equals);

    }

}
