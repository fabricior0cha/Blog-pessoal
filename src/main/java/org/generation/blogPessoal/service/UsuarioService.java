package org.generation.blogPessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.generation.blogPessoal.model.Usuario;
import org.generation.blogPessoal.model.UsuarioLogin;
import org.generation.blogPessoal.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha);
	}

	public Usuario cadastrarUsuario(Usuario usuario) {
		Optional<Usuario> optional = repository.findByUsuario(usuario.getUsuario());
		if (optional.isEmpty()) {
			usuario.setSenha(criptografarSenha(usuario.getSenha()));
			return repository.save(usuario);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já cadastrado");
		}

	}

	public Usuario atualizarUsuario (Usuario usuario){
		Optional<Usuario> optional = repository.findById(usuario.getId());
			if(optional.isPresent()) {
				usuario.setSenha(criptografarSenha(usuario.getSenha()));
				return repository.save(usuario);
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não encontrado");
			}
	}
	
	public Optional<UsuarioLogin> logar(Optional<UsuarioLogin> user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		Optional<Usuario> usuario = repository.findByUsuario(user.get().getUsuario());

		if (usuario.isPresent()) {
			if (encoder.matches(user.get().getSenha(), usuario.get().getSenha())) {
				user.get().setToken(gerarTokenBasic64(user.get().getUsuario(), user.get().getSenha()));
				user.get().setNome(usuario.get().getNome());

				return user;
			}
		}
		throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não cadastrado");
	}
	
	public String gerarTokenBasic64(String usuario, String senha) {
		String auth = usuario + ":" + senha;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		return  "Basic " + new String(encodedAuth);
	}
}
