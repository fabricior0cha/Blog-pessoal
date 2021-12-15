package org.generation.blogPessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.blogPessoal.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(Lifecycle.PER_CLASS)
class UsuarioRepositoryTest {
	
	@Autowired UsuarioRepository repository;
	
	//Given
	@BeforeAll
	void start() {
		repository.save(new Usuario(0L , "Pedro Rocha" , "pedro@email.com" , "90900909"));
		
		repository.save(new Usuario(0L , "Marcos Rocha" , "marcos@email.com" , "90900909"));
		
		repository.save(new Usuario(0L , "João Rocha" , "joao@email.com" , "90900909"));
		
		repository.save(new Usuario(0L , "Camile Rocha" , "camile@email.com" , "90900909"));
		
	}
	
	@Test
	@DisplayName("Retornar um usuário")
	public void procuraPorUmUsuarioRetornaUmUsuario() {
		Optional<Usuario> optional = repository.findByUsuario("pedro@email.com");
		
		assertTrue(optional.isPresent());
	}
	
	@Test
	@DisplayName("Retornar três usuários")
	public void procuraPorRochaRetornaTresUsuarios() {
		List<Usuario> list = repository.findAllByNomeContainingIgnoreCase("rocha");
		
		assertEquals(4, list.size());
		
		assertTrue(list.get(0).getNome().equals("Pedro Rocha"));
		
	}

}
