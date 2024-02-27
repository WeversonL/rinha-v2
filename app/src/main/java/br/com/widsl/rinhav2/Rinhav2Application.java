package br.com.widsl.rinhav2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
@EnableR2dbcRepositories
public class Rinhav2Application {

	public static void main(String[] args) {
		SpringApplication.run(Rinhav2Application.class, args);
	}

}
