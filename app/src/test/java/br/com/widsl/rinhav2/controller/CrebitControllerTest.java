package br.com.widsl.rinhav2.controller;

import static br.com.widsl.rinhav2.util.CrebitCreator.criarExtrato;
import static br.com.widsl.rinhav2.util.CrebitCreator.criarTransacaoRequest;
import static br.com.widsl.rinhav2.util.CrebitCreator.criarTransacaoResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.widsl.rinhav2.domain.TransacaoRequest;
import br.com.widsl.rinhav2.service.impl.CrebitServiceImpl;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
class CrebitControllerTest {

    @InjectMocks
    private CrebitController crebitController;

    @Mock
    private CrebitServiceImpl crebitService;

    @BeforeEach
    void setup() {

        BDDMockito.when(crebitService.gerarTransacao(anyInt(), any(TransacaoRequest.class)))
                .thenReturn(Mono.just(criarTransacaoResponse()));

        BDDMockito.when(crebitService.gerarExtrato(anyInt()))
                .thenReturn(Mono.just(criarExtrato()));

    }

    @Test
    void testaRealizarTransacaoComSucesso() {
        StepVerifier.create(crebitController.criarTransacao(1, criarTransacaoRequest()))
                .expectSubscription()
                .expectNext(criarTransacaoResponse())
                .verifyComplete();
    }

    @Test
    void testaGerarExtratoComSucesso() {

        StepVerifier.create(crebitController.buscarExtrato(1))
                .expectSubscription()
                .expectNextMatches(
                        extrato -> Objects.equals(extrato.saldo().total(), criarExtrato().saldo().total()) &&
                                Objects.equals(extrato.saldo().limite(), criarExtrato().saldo().limite()) &&
                                Objects.equals(extrato.ultimasTransacoes(), criarExtrato().ultimasTransacoes()))
                .verifyComplete();
    }

}
