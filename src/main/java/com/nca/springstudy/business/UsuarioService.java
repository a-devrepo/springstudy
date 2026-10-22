package com.nca.springstudy.business;

import com.nca.springstudy.infrastructure.entities.Usuario;
import com.nca.springstudy.infrastructure.exceptions.ConflictException;
import com.nca.springstudy.infrastructure.exceptions.ResourceNotFoundException;
import com.nca.springstudy.infrastructure.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario salvar(Usuario usuario) {
        validarUsuario(usuario);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    private void validarUsuario(Usuario usuario) {
        if (emailJaCadastrado(usuario.getEmail())) {
            throw new ConflictException("Email ja cadastrado: " + usuario.getEmail());
        }
    }

    private boolean emailJaCadastrado(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario nao encontrado com email: " + email));
    }

    public void deletar(String email) {
        usuarioRepository.deleteByEmail(email);
    }
}
