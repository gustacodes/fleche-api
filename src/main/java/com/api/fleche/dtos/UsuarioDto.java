package com.api.fleche.dtos;

import com.api.fleche.enums.Genero;
import com.api.fleche.enums.Preferencia;
import com.api.fleche.enums.Status;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDto {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @Email(message = "E-mail inválido")
    private String email;

    @Size(min = 10, max = 20, message = "O número deve ter entre 10 e 20 caracteres")
    private String numero;

    @NotNull(message = "A data de nascimento é obrigatória")
    private LocalDate dataNascimento;

    private Status status;

    public UsuarioDto() {
        this.status = Status.ATIVO;
    }

}
