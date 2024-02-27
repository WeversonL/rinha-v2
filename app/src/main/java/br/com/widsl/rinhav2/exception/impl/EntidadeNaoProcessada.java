package br.com.widsl.rinhav2.exception.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class EntidadeNaoProcessada extends RuntimeException {
    public EntidadeNaoProcessada(String message) {
        super(message);
    }
}
