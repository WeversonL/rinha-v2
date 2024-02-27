package br.com.widsl.rinhav2.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.widsl.rinhav2.domain.Extrato;
import br.com.widsl.rinhav2.domain.TransacaoRequest;
import br.com.widsl.rinhav2.domain.TransacaoResponse;
import br.com.widsl.rinhav2.service.CrebitService;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientes")
public class CrebitController {

    private final CrebitService service;

    public CrebitController(CrebitService service) {
        this.service = service;
    }

    @PostMapping("/{id}/transacoes")
    @ResponseStatus(HttpStatus.OK)
    public Mono<TransacaoResponse> criarTransacao(@PathVariable("id") Integer id,
            @RequestBody @Valid TransacaoRequest transacaoRequest) {
        return service.gerarTransacao(id, transacaoRequest);
    }

    @GetMapping("/{id}/extrato")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Extrato> buscarExtrato(@PathVariable("id") Integer id) {
        return service.gerarExtrato(id);
    }

}
