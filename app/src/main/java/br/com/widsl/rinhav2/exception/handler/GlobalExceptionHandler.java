package br.com.widsl.rinhav2.exception.handler;

import org.springframework.core.codec.DecodingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import br.com.widsl.rinhav2.exception.impl.ClienteNaoEncontrado;
import br.com.widsl.rinhav2.exception.impl.EntidadeNaoProcessada;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClienteNaoEncontrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleClienteNaoEncontrado() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler({ DecodingException.class, EntidadeNaoProcessada.class, MethodArgumentNotValidException.class,
            WebExchangeBindException.class })
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<String> handleErrosValidacao() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
    }

}
