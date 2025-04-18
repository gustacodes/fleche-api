package com.api.fleche.controllers;

import com.api.fleche.dtos.LoginDto;
import com.api.fleche.models.Usuario;
import com.api.fleche.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:8100")
public class AuthenticationController {

    private final UsuarioService usuarioService;

    @PostMapping(value = "/singup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> criarConta(
            @RequestParam("nome") String nome,
            @RequestParam("email") String email,
            @RequestParam("numero") String numero,
            @RequestParam("senha") String senha,
            @RequestParam("dataNascimento") String dataNascimento,
            @RequestParam("foto") MultipartFile foto) {

        if (usuarioService.existsByEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email já cadastrado!");
        }
        if (usuarioService.existsByNumero(numero)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Telefone já cadastrado!");
        }
        LocalDate nascimento = LocalDate.parse(dataNascimento);
        if (!usuarioService.verificaIdade(nascimento)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Precisa ter 18 anos ou mais");
        }

        Usuario usuarioModel = new Usuario();
        usuarioModel.setNome(nome);
        usuarioModel.setEmail(email);
        usuarioModel.setNumero(numero);
        usuarioModel.setSenha(senha);
        usuarioModel.setDataNascimento(nascimento);
        try {
            usuarioModel.setFoto(foto.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao salvar imagem");
        }
        usuarioModel.setDataDeCriacao(LocalDateTime.now(ZoneId.of("UTC")));
        usuarioService.criarConta(usuarioModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioModel);
    }

    @PostMapping("/login/{numero}/{senha}")
    public ResponseEntity<?> login(@PathVariable String numero, @PathVariable String senha) {
        LoginDto user = usuarioService.login(numero);
        if (numero.equals(user.getNumero()) && senha.equals(usuarioService.findSenhaByNumero(numero).getSenha())) {
            return ResponseEntity.status(HttpStatus.OK).body(user.getId());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Falha ao realizar login");
    }

}
