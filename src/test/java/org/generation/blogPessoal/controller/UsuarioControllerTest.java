package org.generation.blogPessoal.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.generation.blogPessoal.model.Usuario;
import org.generation.blogPessoal.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;
	@Autowired
	private UsuarioService usuarioService;

	@Test
	@Order(1)
	@DisplayName("Cadastrar um usuário")
	void cadastrarUmUsuario() {
		// Given
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(
				new Usuario(1L, "Fernando Rocha", "fernando@email.com", "3123912"));

		// When
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao,
				Usuario.class);

		// Then
		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());

	}

	@Test
	@Order(2)
	@DisplayName("Não deve duplicar usuário!")
	void naoDuplicarUsuario() {
		// Given
		usuarioService.cadastrarUsuario(new Usuario(0L, "Rodrigo Rocha", "rodrigo@email.com", "109381203"));

		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(
				new Usuario(0L, "Rodrigo Rocha", "rodrigo@email.com", "109381203"));

		// When
		ResponseEntity<Usuario> resposta = testRestTemplate.exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao,
				Usuario.class);

		// Then
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());

	}

	@Test
	@Order(3)
	@DisplayName("Atualizar usuário")
	void deveAtualizarUsuario() {
		// Given
		Usuario usuario = usuarioService
				.cadastrarUsuario(new Usuario(2L, "Paulo Rocha", "paulo@email.com", "109381203"));

		Usuario usuarioAlterado = new Usuario(usuario.getId(), "Fabio Rocha", "paulo@email.com", "109381203");
		HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(usuarioAlterado);

		// When
		ResponseEntity<Usuario> resposta = testRestTemplate.withBasicAuth("boaz", "boaz").exchange("/usuarios/alterar",
				HttpMethod.PUT, requisicao, Usuario.class);

		// Then
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(usuarioAlterado.getNome(), resposta.getBody().getNome());

	}

	@Test
	@Order(4)
	@DisplayName("Lista de todos usuários")
	void deveListarTodosUsuarios() {
		// Given
		usuarioService.cadastrarUsuario(new Usuario(3L, "Lucas Rocha", "lucas@email.com", "109381203"));
		usuarioService.cadastrarUsuario(new Usuario(3L, "Roque Rocha", "roque@email.com", "109381203"));
		
		// When
		ResponseEntity<List> resposta = testRestTemplate.withBasicAuth("boaz", "boaz")
				.exchange("/usuarios/todos", HttpMethod.GET, null, List.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());

	}

}
