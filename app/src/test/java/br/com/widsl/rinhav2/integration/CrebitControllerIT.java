package br.com.widsl.rinhav2.integration;

import static br.com.widsl.rinhav2.util.CrebitCreator.HORARIO;
import static br.com.widsl.rinhav2.util.CrebitCreator.criarClienteModel;
import static br.com.widsl.rinhav2.util.CrebitCreator.criarExtrato;
import static br.com.widsl.rinhav2.util.CrebitCreator.criarTransacaoModel;
import static br.com.widsl.rinhav2.util.CrebitCreator.criarTransacaoResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import br.com.widsl.rinhav2.domain.Extrato;
import br.com.widsl.rinhav2.domain.TransacaoRequest;
import br.com.widsl.rinhav2.domain.TransacaoResponse;
import br.com.widsl.rinhav2.model.ClienteModel;
import br.com.widsl.rinhav2.model.TransacaoModel;
import br.com.widsl.rinhav2.repository.ClienteRepository;
import br.com.widsl.rinhav2.repository.TransacaoRepository;
import br.com.widsl.rinhav2.service.impl.CrebitServiceImpl;
import br.com.widsl.rinhav2.util.CrebitCreator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest
@Import({CrebitServiceImpl.class})
class CrebitControllerIT {

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private TransacaoRepository transacaoRepository;

    @Autowired
    private WebTestClient webTestClient;

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
    @SuppressWarnings("null")
    void testaRealizarTransacaoComSucessoIT() {
        webTestClient.post()
                .uri("/clientes/{id}/transacoes", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(CrebitCreator.criarTransacaoRequest()))
                .exchange()
                .expectStatus().isOk()
                .expectBody(TransacaoResponse.class)
                .isEqualTo(criarTransacaoResponse());
    }

    @Test
    @SuppressWarnings("null")
    void testaRealizarTransacaoQuandoUsuarioNaoExisteIT() {

        BDDMockito.when(clienteRepository.buscaClientePorIdLockUpdate(anyInt()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/clientes/{id}/transacoes", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(CrebitCreator.criarTransacaoRequest()))
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    @SuppressWarnings("null")
    void testaRealizarTransacaoComErroNoLimiteClienteIT() {

        ClienteModel cliente = new ClienteModel(1, 0, 0);
        TransacaoRequest request = new TransacaoRequest(1, "d", "desc");

        BDDMockito.when(clienteRepository.buscaClientePorIdLockUpdate(anyInt()))
                .thenReturn(Mono.just(cliente));

        webTestClient.post()
                .uri("/clientes/{id}/transacoes", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @SuppressWarnings("null")
    void testaRealizarTransacaoComTipoInvalidoIT() {

        webTestClient.post()
                .uri("/clientes/{id}/transacoes", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new TransacaoRequest(1, "f", "desc")))
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @SuppressWarnings("null")
    void testaRealizarTransacaoComValorInvalidoIT() {

        webTestClient.post()
                .uri("/clientes/{id}/transacoes", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\"valor\": 1.2, \"tipo\": \"c\", \"descricao\": \"desc\"}"))
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @SuppressWarnings("null")
    void testaRealizarTransacaoComDescricaoInvalidaIT() {

        webTestClient.post()
                .uri("/clientes/{id}/transacoes", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\"valor\": 1.2, \"tipo\": \"c\", \"descricao\": \"\"}"))
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @SuppressWarnings("null")
    void testaRealizarTransacaoComIdInvalidoIT() {

        webTestClient.post()
                .uri("/clientes/{id}/transacoes", 1.2)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("{\"valor\": 1, \"tipo\": \"c\", \"descricao\": \"desc\"}"))
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @SuppressWarnings("null")
    void testaGerarExtratoComSucessoIT() {

        Extrato extrato = criarExtrato();

        webTestClient.get()
                .uri("/clientes/{id}/extrato", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.saldo.total").isEqualTo(extrato.saldo().total())
                .jsonPath("$.saldo.limite").isEqualTo(extrato.saldo().limite())
                .jsonPath("$.ultimas_transacoes[0].valor").isEqualTo(extrato
                        .ultimasTransacoes().get(0).valor())
                .jsonPath("$.ultimas_transacoes[0].tipo").isEqualTo(extrato
                        .ultimasTransacoes().get(0).tipo())
                .jsonPath("$.ultimas_transacoes[0].descricao").isEqualTo(extrato
                        .ultimasTransacoes().get(0).descricao());
    }

    @Test
    @SuppressWarnings("null")
    void testaGerarExtratoQuandoNaoTemTransacoesIT() {

        Extrato extrato = new Extrato(new Extrato.Saldo(1, 1, HORARIO), new ArrayList<>());

        BDDMockito.when(transacaoRepository.buscaTransacao(anyInt()))
                .thenReturn(Flux.empty());

        webTestClient.get()
                .uri("/clientes/{id}/extrato", 1)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.saldo.total").isEqualTo(extrato.saldo().total())
                .jsonPath("$.saldo.limite").isEqualTo(extrato.saldo().limite())
                .jsonPath("$.ultimas_transacoes").isEmpty();
    }

    @Test
    @SuppressWarnings("null")
    void testaGerarExtratoQuandoClienteNaoExisteIT() {

        BDDMockito.when(clienteRepository.buscaClientePorIdLockUpdate(anyInt()))
                .thenReturn(Mono.empty());

        webTestClient.post()
                .uri("/clientes/{id}/extrato", 2)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @SuppressWarnings("null")
    void testaGerarExtratoComIdInvalidoIT() {

        webTestClient.post()
                .uri("/clientes/{id}/extrato", 1.2)
                .exchange()
                .expectStatus().is4xxClientError();
    }

}
