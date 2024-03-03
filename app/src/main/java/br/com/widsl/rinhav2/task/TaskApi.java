package br.com.widsl.rinhav2.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import br.com.widsl.rinhav2.domain.Extrato;
import br.com.widsl.rinhav2.domain.TransacaoRequest;
import br.com.widsl.rinhav2.domain.TransacaoResponse;
import br.com.widsl.rinhav2.service.CrebitService;
import br.com.widsl.rinhav2.service.impl.CrebitServiceImpl;

@Component
public class TaskApi implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskApi.class);

    private final CrebitService crebitService;
    private final WebClient webClient;
    private final String serverPort;

    public TaskApi(CrebitServiceImpl crebitService, WebClient webClient,
            @Value("${server.port}") String serverPort) {
        this.crebitService = crebitService;
        this.webClient = webClient;
        this.serverPort = serverPort;
    }

    @Override
    @SuppressWarnings("null")
    public void run(ApplicationArguments args) {

        TransacaoRequest transacaoCredito = new TransacaoRequest(1, "c", "desc");
        TransacaoRequest transacaoDebito = new TransacaoRequest(1, "d", "desc");

        String postURI = "http://localhost:%s/clientes/1/transacoes".formatted(serverPort);
        String getURI = "http://localhost:%s/clientes/1/extrato".formatted(serverPort);

        this.webClient.post()
                .uri(postURI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(transacaoCredito))
                .retrieve()
                .bodyToMono(TransacaoResponse.class)
                .block();

        this.webClient.post()
                .uri(postURI)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(transacaoDebito))
                .retrieve()
                .bodyToMono(TransacaoResponse.class)
                .block();

        this.webClient.get()
                .uri(getURI)
                .retrieve()
                .bodyToMono(Extrato.class)
                .block();

        this.crebitService
                .removerTransacao(1)
                .blockLast();

        LOGGER.info("-> carga inicial completa <-");

    }

}
