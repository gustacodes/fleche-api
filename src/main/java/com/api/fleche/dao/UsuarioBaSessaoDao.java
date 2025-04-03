package com.api.fleche.dao;

import com.api.fleche.dtos.UsuarioBarDto;
import com.api.fleche.dtos.UsuarioDadosDto;
import com.api.fleche.dtos.UsuarioDto;
import com.api.fleche.repositories.ComandosSqlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UsuarioBaSessaoDao {

    private final JdbcTemplate jdbcTemplate;
    private final ComandosSqlRepository comandosSqlRepository;

    public Page<UsuarioBarDto> usuariosParaListar(String qrCode, Long usuarioId, Pageable pageable) {
        String sql = comandosSqlRepository.listaUsuarios().getCmdSql();

        List<UsuarioBarDto> resultados = jdbcTemplate.query(sql, new Object[]{qrCode, usuarioId}, (rs, rowNum) ->
                new UsuarioBarDto(
                        rs.getString("NOME"),
                        rs.getString("GENERO"),
                        rs.getInt("IDADE")
                )
        );

        // Caso a lista esteja vazia, retorna uma página vazia
        if (resultados.isEmpty()) {
            return Page.empty(pageable);
        }

        // Configura os índices de início e fim com base na paginação
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resultados.size());

        List<UsuarioBarDto> usuariosPaginados = resultados.subList(start, end);

        return new PageImpl<>(usuariosPaginados, pageable, resultados.size());
    }


}
