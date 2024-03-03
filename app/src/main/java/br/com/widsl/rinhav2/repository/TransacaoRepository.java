package br.com.widsl.rinhav2.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import br.com.widsl.rinhav2.model.TransacaoModel;
import reactor.core.publisher.Flux;

@Repository
public interface TransacaoRepository extends R2dbcRepository<TransacaoModel, Integer> {
    @Query("SELECT cliente_id, valor, realizada_em, descricao, tipo " +
            "FROM transacoes WHERE cliente_id = :id ORDER BY realizada_em DESC LIMIT 10")
    Flux<TransacaoModel> buscaTransacao(final Integer id);

    @Query("DELETE FROM transacoes WHERE cliente_id = :id")
    Flux<Void> deletaTransacaoPorId(final Integer id);

}
