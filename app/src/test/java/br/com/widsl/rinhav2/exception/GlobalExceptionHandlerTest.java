package br.com.widsl.rinhav2.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.widsl.rinhav2.exception.handler.GlobalExceptionHandler;
import br.com.widsl.rinhav2.exception.impl.ClienteNaoEncontrado;
import br.com.widsl.rinhav2.exception.impl.EntidadeNaoProcessada;

@ExtendWith(SpringExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleClienteNaoEncontrado_DeveRetornarStatusNotFound() {
        ClienteNaoEncontrado exception = new ClienteNaoEncontrado("Cliente não encontrado");
        ResponseEntity<String> response = handler.handleClienteNaoEncontrado(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleErrosValidacao_DeveRetornarStatusUnprocessableEntity() {
        EntidadeNaoProcessada exception = new EntidadeNaoProcessada("Erro de validação");
        ResponseEntity<String> response = handler.handleErrosValidacao(exception);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

}
