package br.com.widsl.rinhav2.service;

import static br.com.widsl.rinhav2.util.CrebitCreator.criarClienteModel;
import static br.com.widsl.rinhav2.util.CrebitCreator.criarExtrato;
import static br.com.widsl.rinhav2.util.CrebitCreator.criarTransacaoModel;
import static br.com.widsl.rinhav2.util.CrebitCreator.criarTransacaoRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.Objects;

import br.com.widsl.rinhav2.domain.TransacaoRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.widsl.rinhav2.exception.impl.ClienteNaoEncontrado;
import br.com.widsl.rinhav2.exception.impl.EntidadeNaoProcessada;
import br.com.widsl.rinhav2.model.ClienteModel;
import br.com.widsl.rinhav2.model.TransacaoModel;
import br.com.widsl.rinhav2.repository.ClienteRepository;
import br.com.widsl.rinhav2.repository.TransacaoRepository;
import br.com.widsl.rinhav2.service.impl.CrebitServiceImpl;
import br.com.widsl.rinhav2.util.CrebitCreator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class CrebitServiceTest {

    @InjectMocks
    private CrebitServiceImpl crebitService;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @SuppressWarnings("null")
    @BeforeEach
    void setup() {

        ClienteModel cliente = criarClienteModel();

        BDDMockito.when(clienteRepository.buscaClientePorId(anyInt()))
                .thenReturn(Mono.just(cliente));

        BDDMockito.when(clienteRepository.buscaClientePorIdLockUpdate(anyInt()))
                .thenReturn(Mono.just(cliente));

        BDDMockito.when(clienteRepository.atualizaSaldoCliente(anyInt(), anyInt()))
                .thenReturn(Mono.just(cliente));

        BDDMockito.when(transacaoRepository.save(any(TransacaoModel.class)))
                .thenReturn(Mono.just(criarTransacaoModel()));

        BDDMockito.when(transacaoRepository.buscaTransacao(anyInt()))
                .thenReturn(Flux.just(criarTransacaoModel()));

    }

    @Test
    void testaRealizarTransacaoComSucesso() {
        StepVerifier.create(crebitService.gerarTransacao(1, criarTransacaoRequest()))
                .expectSubscription()
                .expectNext(CrebitCreator.criarTransacaoResponse())
                .verifyComplete();
    }

    @Test
    void testaRealizarTransacaoQuandoUsuarioNaoExiste() {

        BDDMockito.when(clienteRepository.buscaClientePorId(anyInt()))
                .thenReturn(Mono.empty());

        BDDMockito.when(clienteRepository.buscaClientePorIdLockUpdate(anyInt()))
                .thenReturn(Mono.empty());

        StepVerifier.create(crebitService.gerarTransacao(1, criarTransacaoRequest()))
                .expectSubscription()
                .expectError(ClienteNaoEncontrado.class)
                .verify();
    }

    @Test
    void testaRealizarTransacaoComErroNoLimiteCliente() {

        ClienteModel cliente = new ClienteModel(1, 0, 0);
        TransacaoRequest request = new TransacaoRequest(1, "d", "desc");

        BDDMockito.when(clienteRepository.buscaClientePorIdLockUpdate(anyInt()))
                .thenReturn(Mono.just(cliente));

        StepVerifier.create(crebitService.gerarTransacao(1, request))
                .expectSubscription()
                .expectError(EntidadeNaoProcessada.class)
                .verify();
    }

    @Test
    void testaGerarExtratoComSucesso() {

        StepVerifier.create(crebitService.gerarExtrato(1))
                .expectSubscription()
                .expectNextMatches(
                        extrato -> Objects.equals(extrato.saldo().total(), criarExtrato().saldo().total()) &&
                                Objects.equals(extrato.saldo().limite(), criarExtrato().saldo().limite()) &&
                                Objects.equals(extrato.ultimasTransacoes(), criarExtrato().ultimasTransacoes()))
                .verifyComplete();
    }

    @Test
    void testaGerarExtratoQuandoClienteNaoExiste() {

        BDDMockito.when(clienteRepository.buscaClientePorId(anyInt()))
                .thenReturn(Mono.empty());

        StepVerifier.create(crebitService.gerarExtrato(1))
                .expectSubscription()
                .expectError(ClienteNaoEncontrado.class)
                .verify();
    }

    @Test
    void testaGerarExtratoQuandoNaoTemTransacoes() {

        BDDMockito.when(transacaoRepository.buscaTransacao(anyInt()))
                .thenReturn(Flux.empty());

        StepVerifier.create(crebitService.gerarExtrato(1))
                .expectSubscription()
                .expectNextMatches(
                        extrato -> Objects.equals(extrato.saldo().total(), criarExtrato().saldo().total()) &&
                                Objects.equals(extrato.saldo().limite(), criarExtrato().saldo().limite()) &&
                                extrato.ultimasTransacoes().isEmpty())
                .verifyComplete();
    }

}
