package br.com.widsl.rinhav2.domain;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record TransacaoRequest(
        @NotNull(message = "O campo valor é obrigatório")
        @PositiveOrZero(message = "O campo valor deve ser positivo")
        Integer valor,

        @NotBlank(message = "O campo tipo é obrigatório")
        @Pattern(regexp = "[c|d]", message = "O campo tipo deve ser 'D' ou 'C'")
        String tipo,

        @NotBlank(message = "O campo descricao é obrigatório")
        @Length(max = 10, message = "O campo descricao deve conter no máximo 10 caracteres")
        String descricao) {
}