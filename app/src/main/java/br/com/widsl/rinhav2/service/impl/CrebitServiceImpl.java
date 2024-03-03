package br.com.widsl.rinhav2.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Mono<TransacaoResponse> gerarTransacao(final Integer id, final TransacaoRequest request) {

        Integer valor = "d".equals(request.tipo()) ? (-request.valor()) : request.valor();

        return this.clienteRepository.buscaClientePorIdLockUpdate(id)
                .flatMap(cliente -> this.persisteSaldoCliente(id, valor, cliente)
                        .flatMap(clienteAtualizado -> this.persisteTransacao(id, request, clienteAtualizado)))
                .switchIfEmpty(Mono.error(new ClienteNaoEncontrado("Cliente não encontrado")));
    }

    private Mono<TransacaoResponse> persisteTransacao(final Integer id, final TransacaoRequest request,
            final ClienteModel cliente) {
        return this.transacaoRepository.save(
                new TransacaoModel(id, request.valor(), null, request.descricao(),
                        request.tipo()))
                .thenReturn(new TransacaoResponse(cliente.limite(), cliente.saldo()));
    }

    private Mono<ClienteModel> persisteSaldoCliente(final Integer id, final Integer valor, final ClienteModel cliente) {
        if (cliente.saldo() + valor < -cliente.limite())
            return Mono.error(new EntidadeNaoProcessada("Entidade não processada"));
        return clienteRepository.atualizaSaldoCliente(id, (cliente.saldo() + valor));
    }

    @Override
    @Transactional
    public Mono<Extrato> gerarExtrato(final Integer id) {

        Mono<Extrato.Saldo> clienteResult = this.buscaCliente(id)
                .map(cliente -> new Extrato.Saldo(cliente.saldo(), cliente.limite(), LocalDateTime.now()));

        Flux<Extrato.Transacao> transacaoFlux = this.transacaoRepository.buscaTransacao(id)
                .map(transacaos -> new Extrato.Transacao(
                        transacaos.valor(),
                        transacaos.tipo(),
                        transacaos.descricao(),
                        transacaos.realizadaEm().atZoneSameInstant(ZoneOffset.UTC)));

        return clienteResult.zipWith(transacaoFlux.collectList(), Extrato::new);
    }

    private Mono<ClienteModel> buscaCliente(final Integer id) {

        return this.clienteRepository.buscaClientePorId(id)
                .switchIfEmpty(Mono.error(new ClienteNaoEncontrado("Cliente não encontrado")));

    }

}
