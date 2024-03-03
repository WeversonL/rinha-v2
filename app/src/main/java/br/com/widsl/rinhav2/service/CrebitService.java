package br.com.widsl.rinhav2.service;

import br.com.widsl.rinhav2.domain.Extrato;
import br.com.widsl.rinhav2.domain.TransacaoRequest;
import br.com.widsl.rinhav2.domain.TransacaoResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CrebitService {
    Mono<TransacaoResponse> gerarTransacao(final Integer id, final TransacaoRequest request);

    Flux<Void> removerTransacao(final Integer id);

    Mono<Extrato> gerarExtrato(final Integer id);
}
