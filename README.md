<h1 align="center">
   Rinha Backend V2
</h1>

Project developed to participate in the "Rinha de Backend V2" challenge. The objective of the challenge is to create an API for
handle debit and credit transactions, using database and load balancer to manage two API instances, all in a limited way
environment with 1.5vCPUs and 550MB of memory.

## Technologies

- [Spring Boot](https://spring.io/projects/spring-boot)
- [Spring Boot Webflux](https://docs.spring.io/spring-framework/reference/web/webflux.html)
- [Spring Data R2DBC](https://spring.io/projects/spring-data-r2dbc)
- [Docker](https://docs.docker.com/get-started/)
- [PostgreSQL](https://www.postgresql.org/docs/13/index.html)
- [Nginx](https://nginx.org/en/docs/)

## Practices adopted

- SOLID
- API REST
- Automated Tests [Unit Tests and Integration Tests]
- Resource optimization
- Queries with Spring Data R2DBC
- Dependency Injection
- Containerization with docker
- Load Distribution with Load Balancer

## Get started

In the project, an [example environment variables file](.env) will be available. The project will start using it, if you
want to change the database user and password, or some other value, feel free

0. Clone git repository with modules

   git clone https://github.com/WeversonL/rinha-v2.git
   cd rinha-v2

### Running the application with docker-compose

1. Start with docker-compose

   docker compose -f docker/docker-compose.yml up -d

You can access the API via load balancer, sending requests to localhost:9999. Due to resource limitations, it is
necessary to wait a while for the entire application to be up and running.

## API Endpoints

**The endpoints offered are based on those requested by the challenge!!**

To make the HTTP requests below, the [httpie](https://httpie.io) tool was used:

- Create a credit transaction

```
$ http POST :9999/clientes/1/transacoes valor=1000 tipo="c" descricao="Credit cash"

{
    "limite": 100000,
    "saldo": 1000
}
```

- Create a debit transaction

```
$ http POST :9999/clientes/1/transacoes valor=1001 tipo="d" descricao="Debit cash"

{
    "limite": 100000,
    "saldo": -1
}
```

- Obtaining customer statement

```
$ http GET :9999/clientes/1/extrato

{
    "saldo": {
        "total": -1,
        "limite": 100000,
        "data_extrato": "2024-02-22T16:36:08.250670122"
    },
    "ultimas_transacoes": [
        {
            "valor": 1000,
            "tipo": "c",
            "descricao": "Credit cash",
            "realizada_em": "2024-02-22T16:34:35.122608"
        },
        {
            "valor": 1000,
            "tipo": "d",
            "descricao": "Debit cash",
            "realizada_em": "2024-02-22T16:21:31.282657"
        }
    ]
}

```

## License

`Rinha Backend V2` is [MIT licensed](LICENSE).
