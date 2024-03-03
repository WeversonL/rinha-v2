package br.com.widsl.rinhav2;

import br.com.widsl.rinhav2.domain.Extrato;
import br.com.widsl.rinhav2.domain.TransacaoRequest;
import br.com.widsl.rinhav2.domain.TransacaoResponse;
import br.com.widsl.rinhav2.service.CrebitService;
import br.com.widsl.rinhav2.service.impl.CrebitServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableR2dbcRepositories
public class Rinhav2Application implements ApplicationRunner {

	private final CrebitService crebitService;
	private final WebClient webClient;
	private final String serverPort;

    public Rinhav2Application(CrebitServiceImpl crebitService, WebClient webClient, @Value("${server.port}") String serverPort) {
        this.crebitService = crebitService;
        this.webClient = webClient;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) {
		SpringApplication.run(Rinhav2Application.class, args);
	}

	@Override
	@SuppressWarnings("null")
	public void run(ApplicationArguments args) {

		TransacaoRequest transacaoCredito = new TransacaoRequest(1,"c", "desc");
		TransacaoRequest transacaoDebito = new TransacaoRequest(1,"d", "desc");

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

		System.out.println("-> carga inicial completa <-");


	}

}
