package br.com.widsl.rinhav2.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.widsl.rinhav2.util.CrebitCreator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@ExtendWith(SpringExtension.class)
class TransacaoRequestTest {

    private final ValidatorFactory factory = Validation.byDefaultProvider()
            .configure()
            .messageInterpolator(new ResourceBundleMessageInterpolator())
            .buildValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testeTransacaoRequestValida() {
        Integer valor = 100;
        String tipo = "c";
        String descricao = "Compra";

        TransacaoRequest transacaoRequest = new TransacaoRequest(valor, tipo, descricao);

        Set<ConstraintViolation<TransacaoRequest>> violations = validator.validate(transacaoRequest);

        assertTrue(violations.isEmpty());
    }

    @Test
    void testeTransacaoRequestInvalida() {

        Integer valor = -100;
        String tipo = "X";
        String descricao = "";

        TransacaoRequest transacaoRequest = new TransacaoRequest(valor, tipo, descricao);

        Set<ConstraintViolation<TransacaoRequest>> violations = validator.validate(transacaoRequest);
        assertFalse(violations.isEmpty());
        assertEquals(3, violations.size());
    }

    @Test
    void testeInstanciacaoEqualsTransacaoRequest() {

        TransacaoRequest transacaoRequest = new TransacaoRequest(1, "c", "desc");
        TransacaoRequest esperado = CrebitCreator.criarTransacaoRequest();

        assertEquals(esperado.valor(), transacaoRequest.valor());
        assertEquals(esperado.tipo(), transacaoRequest.tipo());
        assertEquals(esperado.descricao(), transacaoRequest.descricao());

        boolean equals = esperado.equals(transacaoRequest);

        Assertions.assertTrue(equals);

    }

}