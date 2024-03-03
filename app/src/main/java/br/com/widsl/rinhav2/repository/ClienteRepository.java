package br.com.widsl.rinhav2.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import br.com.widsl.rinhav2.model.ClienteModel;
import reactor.core.publisher.Mono;

@Repository
public interface ClienteRepository extends R2dbcRepository<ClienteModel, Integer> {
    @Query("UPDATE clientes SET saldo = :valor WHERE id = :id RETURNING *")
    Mono<ClienteModel> atualizaSaldoCliente(final Integer id, Integer valor);

    @Query("SELECT id, limite, saldo FROM clientes WHERE id = :id FOR UPDATE")
    Mono<ClienteModel> buscaClientePorIdLockUpdate(final Integer id);

    @Query("SELECT id, limite, saldo FROM clientes WHERE id = :id")
    Mono<ClienteModel> buscaClientePorId(final Integer id);
}
