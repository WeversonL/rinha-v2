package br.com.widsl.rinhav2.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import br.com.widsl.rinhav2.model.TransacaoModel;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TransacaoRepository extends R2dbcRepository<TransacaoModel, Integer> {
    @Query("SELECT cliente_id, valor, realizada_em, descricao, tipo " +
            "FROM transacoes WHERE cliente_id = :id ORDER BY id DESC LIMIT 10")
    Flux<TransacaoModel> buscaTransacao(final Integer id);

    @SuppressWarnings({ "null", "unchecked" })
    @Query("INSERT INTO transacoes (cliente_id, valor, descricao, tipo) VALUES " +
            "(:#{#transacaoModel.clienteId}, :#{#transacaoModel.valor}, :#{#transacaoModel.descricao}, " +
            ":#{#transacaoModel.tipo})")
    Mono<Void> save(TransacaoModel transacaoModel);

    @Query("DELETE FROM transacoes WHERE cliente_id = :id")
    Flux<Void> deletaTransacaoPorId(final Integer id);

}
