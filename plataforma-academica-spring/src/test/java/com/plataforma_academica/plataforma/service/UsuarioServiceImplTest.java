package com.plataforma_academica.plataforma.service;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void login_DeveRetornarUsuario_QuandoCredenciaisValidas() throws Exception {
        // Arrange
        String email = "user@example.com";
        String senha = "password";
        String senhaCriptografada = new BCryptPasswordEncoder().encode(senha);

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(senhaCriptografada);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Mock the password encoder to return true for matches
        Field encoderField = UsuarioServiceImpl.class.getDeclaredField("passwordEncoder");
        encoderField.setAccessible(true);
        BCryptPasswordEncoder mockEncoder = mock(BCryptPasswordEncoder.class);
        when(mockEncoder.matches(senha, senhaCriptografada)).thenReturn(true);
        encoderField.set(usuarioService, mockEncoder);

        // Act
        Optional<Usuario> result = usuarioService.login(email, senha);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(usuario, result.get());
    }

    @Test
    void login_DeveRetornarEmpty_QuandoEmailNaoEncontrado() {
        // Arrange
        String email = "user@example.com";
        String senha = "password";

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act
        Optional<Usuario> result = usuarioService.login(email, senha);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void login_DeveRetornarEmpty_QuandoSenhaIncorreta() throws Exception {
        // Arrange
        String email = "user@example.com";
        String senha = "password";
        String senhaCriptografada = new BCryptPasswordEncoder().encode("different");

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(senhaCriptografada);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

        // Mock the password encoder to return false for matches
        Field encoderField = UsuarioServiceImpl.class.getDeclaredField("passwordEncoder");
        encoderField.setAccessible(true);
        BCryptPasswordEncoder mockEncoder = mock(BCryptPasswordEncoder.class);
        when(mockEncoder.matches(senha, senhaCriptografada)).thenReturn(false);
        encoderField.set(usuarioService, mockEncoder);

        // Act
        Optional<Usuario> result = usuarioService.login(email, senha);

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void cadastrarUsuario_DeveSalvarUsuario_QuandoEmailDisponivel() throws Exception {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("user@example.com");
        usuario.setSenha("password");

        when(usuarioRepository.findByEmail("user@example.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Mock the password encoder to encode the password
        Field encoderField = UsuarioServiceImpl.class.getDeclaredField("passwordEncoder");
        encoderField.setAccessible(true);
        BCryptPasswordEncoder mockEncoder = mock(BCryptPasswordEncoder.class);
        when(mockEncoder.encode("password")).thenReturn("encoded");
        encoderField.set(usuarioService, mockEncoder);

        // Act
        Usuario result = usuarioService.cadastrarUsuario(usuario);

        // Assert
        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void cadastrarUsuario_DeveLancarIllegalArgumentException_QuandoEmailJaCadastrado() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("user@example.com");
        usuario.setSenha("password");

        Usuario existente = new Usuario();
        existente.setEmail("user@example.com");

        when(usuarioRepository.findByEmail("user@example.com")).thenReturn(Optional.of(existente));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> usuarioService.cadastrarUsuario(usuario));
        assertEquals("Email já cadastrado", exception.getMessage());
    }

    @Test
    void buscarPorId_DeveRetornarUsuario_QuandoEncontrado() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        Usuario usuario = new Usuario();
        usuario.setId(id);

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        // Act
        Usuario result = usuarioService.buscarPorId(id);

        // Assert
        assertEquals(usuario, result);
    }

    @Test
    void buscarPorId_DeveRetornarNull_QuandoNaoEncontrado() {
        // Arrange
        Long id = UUID.fromString("00000000-0000-0000-0000-000000000001");

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        Usuario result = usuarioService.buscarPorId(id);

        // Assert
        assertNull(result);
    }

    @Test
    void listarTodos_DeveRetornarLista() {
        // Arrange
        List<Usuario> usuarios = List.of(new Usuario(), new Usuario());

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<Usuario> result = usuarioService.listarTodos();

        // Assert
        assertEquals(usuarios, result);
        verify(usuarioRepository).findAll();
    }
}