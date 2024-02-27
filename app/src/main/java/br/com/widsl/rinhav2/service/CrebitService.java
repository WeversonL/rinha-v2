package br.com.widsl.rinhav2.service;

import br.com.widsl.rinhav2.domain.Extrato;
import br.com.widsl.rinhav2.domain.TransacaoRequest;
import br.com.widsl.rinhav2.domain.TransacaoResponse;
import reactor.core.publisher.Mono;

public interface CrebitService {
    Mono<TransacaoResponse> gerarTransacao(Integer id, TransacaoRequest request);

    Mono<Extrato> gerarExtrato(Integer id);
}
