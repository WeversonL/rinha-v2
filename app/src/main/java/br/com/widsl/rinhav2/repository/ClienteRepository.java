package br.com.widsl.rinhav2.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.widsl.rinhav2.model.ClienteModel;
import reactor.core.publisher.Mono;

@Repository
public interface ClienteRepository extends R2dbcRepository<ClienteModel, Integer> {
    @Query("UPDATE clientes SET saldo = (saldo + :valor) WHERE id = :id AND (saldo + :valor) >= (-limite) RETURNING *")
    Mono<ClienteModel> atualizaSaldoCliente(Integer id, Integer valor);

    @Query("SELECT id, limite, saldo FROM clientes WHERE id = :id")
    @Transactional(readOnly = true)
    Mono<ClienteModel> buscaClientePorId(Integer id);
}
