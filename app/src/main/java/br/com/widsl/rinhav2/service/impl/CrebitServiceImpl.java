package br.com.widsl.rinhav2.service.impl;

import br.com.widsl.rinhav2.domain.Extrato;
import br.com.widsl.rinhav2.domain.TransacaoRequest;
import br.com.widsl.rinhav2.domain.TransacaoResponse;
import br.com.widsl.rinhav2.exception.impl.ClienteNaoEncontrado;
import br.com.widsl.rinhav2.exception.impl.EntidadeNaoProcessada;
import br.com.widsl.rinhav2.model.ClienteModel;
import br.com.widsl.rinhav2.model.TransacaoModel;
import br.com.widsl.rinhav2.repository.ClienteRepository;
import br.com.widsl.rinhav2.repository.TransacaoRepository;
import br.com.widsl.rinhav2.service.CrebitService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class CrebitServiceImpl implements CrebitService {

    private final ClienteRepository clienteRepository;
    private final TransacaoRepository transacaoRepository;

    public CrebitServiceImpl(ClienteRepository clienteRepository, TransacaoRepository transacaoRepository) {
        this.clienteRepository = clienteRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Override
    @Transactional
    public Mono<TransacaoResponse> gerarTransacao(Integer id, TransacaoRequest request) {

        Integer valor = "d".equals(request.tipo()) ? (-request.valor()) : request.valor();

        return buscaClientePorId(id)
                .flatMap(cliente -> persisteSaldoCliente(id, valor)
                        .flatMap(clienteAtualizado -> persisteTransacao(id, request, clienteAtualizado))
                );
    }

    private Mono<TransacaoResponse> persisteTransacao(Integer id, TransacaoRequest request, ClienteModel cliente) {
        return transacaoRepository.save(
                new TransacaoModel(id, request.valor(), null, request.descricao(),
                        request.tipo())
        ).thenReturn(new TransacaoResponse(cliente.limite(), cliente.saldo()));
    }

    private Mono<ClienteModel> persisteSaldoCliente(Integer id, Integer valor) {
        return clienteRepository.atualizaSaldoCliente(id, valor)
                .switchIfEmpty(Mono.error(new EntidadeNaoProcessada("Entidade não processada")));
    }

    @Override
    @Transactional
    public Mono<Extrato> gerarExtrato(Integer id) {

        Mono<Extrato.Saldo> clienteResult = this.buscaClientePorId(id)
                .map(cliente -> new Extrato.Saldo(cliente.saldo(), cliente.limite(), LocalDateTime.now()));

        Flux<Extrato.Transacao> transacaoFlux = transacaoRepository.buscaTransacao(id)
                .map(transacaos -> new Extrato.Transacao(
                        transacaos.valor(),
                        transacaos.tipo(),
                        transacaos.descricao(),
                        transacaos.realizadaEm().atZoneSameInstant(ZoneOffset.UTC)));

        return clienteResult.zipWith(transacaoFlux.collectList(), Extrato::new);
    }

    private Mono<ClienteModel> buscaClientePorId(Integer id) {

        return clienteRepository.buscaClientePorId(id)
                .switchIfEmpty(Mono.error(new ClienteNaoEncontrado("Cliente não encontrado")));

    }

}
