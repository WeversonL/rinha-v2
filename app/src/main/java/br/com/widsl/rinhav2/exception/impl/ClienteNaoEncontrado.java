package br.com.widsl.rinhav2.exception.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ClienteNaoEncontrado extends RuntimeException {
    public ClienteNaoEncontrado(String message) {
        super(message);
    }
}
