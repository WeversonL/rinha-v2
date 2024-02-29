package br.com.widsl.rinhav2.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.widsl.rinhav2.exception.handler.GlobalExceptionHandler;

@ExtendWith(SpringExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleClienteNaoEncontrado_DeveRetornarStatusNotFound() {
        ResponseEntity<String> response = handler.handleClienteNaoEncontrado();
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleErrosValidacao_DeveRetornarStatusUnprocessableEntity() {
        ResponseEntity<String> response = handler.handleErrosValidacao();
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
    }

}
