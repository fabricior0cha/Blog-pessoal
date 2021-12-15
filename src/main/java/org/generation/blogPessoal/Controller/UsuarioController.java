package org.generation.blogPessoal.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.generation.blogPessoal.model.Usuario;
import org.generation.blogPessoal.model.UsuarioLogin;
import org.generation.blogPessoal.repository.UsuarioRepository;
import org.generation.blogPessoal.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {
	
	private @Autowired UsuarioService usuarioService;
	
	private @Autowired UsuarioRepository usuarioRepository;
	
	@GetMapping("/todos")
	public ResponseEntity<List<Usuario>> getAll (){
		return ResponseEntity.ok(usuarioRepository.findAll());
	}
	
	
	@PostMapping("/cadastrar")
	public ResponseEntity<Usuario> post (@Valid @RequestBody Usuario novoUsuario){
		return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.cadastrarUsuario(novoUsuario));
	}
	
	@PutMapping("/alterar")
	public ResponseEntity<Usuario> put (@Valid @RequestBody Usuario usuario){
		return ResponseEntity.ok().body(usuarioService.atualizarUsuario(usuario));
	}
	
	@PostMapping("/logar")
	public ResponseEntity<UsuarioLogin> autentication (@Valid @RequestBody Optional<UsuarioLogin> user){
		return usuarioService.logar(user).map(resp -> ResponseEntity.ok(resp))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
 }
