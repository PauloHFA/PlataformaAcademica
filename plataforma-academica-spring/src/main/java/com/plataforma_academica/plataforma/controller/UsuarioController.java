package com.plataforma_academica.plataforma.controller;

import com.plataforma_academica.plataforma.dto.UsuarioResponseDTO;
import com.plataforma_academica.plataforma.mapper.UsuarioMapper;
import com.plataforma_academica.plataforma.model.Usuario;
import com.plataforma_academica.plataforma.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * ================================================================================================
 *  USUARIO CONTROLLER
 * ================================================================================================
 * Responsável por gerenciar todas as operações relacionadas aos usuários da plataforma.
 *
 * Funcionalidades principais:
 *  - Cadastro de novos usuários;
 *  - Login/autenticação;
 *  - Busca de usuários por ID.
 *
 * Regras gerais da API:
 *  - O e-mail deve ser único no cadastro;
 *  - As senhas são criptografadas automaticamente no serviço;
 *  - No login, a senha enviada é comparada com o hash salvo no banco;
 *  - Caso o usuário não seja encontrado, retorna-se HTTP adequado (400, 401 ou 404).
 *
 * Rota base: /api/usuarios
 * Esta controller se comunica diretamente com UsuarioService para validações e persistência.
 *
 * CORS habilitado para acesso do front-end Angular (porta 4200).
 * ================================================================================================
 */
@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    // Construtor para garantir que o controller seja instanciado
    public UsuarioController() {
        System.out.println("UsuarioController inicializado!");
    }

    // =========================================================================================
    //  1. CADASTRAR NOVO USUÁRIO
    // =========================================================================================
    /**
     * Endpoint responsável pelo cadastro de um novo usuário no sistema.
     *
     * Regras de validação:
     * - O e-mail não pode estar cadastrado previamente;
     * - A senha informada é automaticamente criptografada antes de salvar;
     * - Caso o e-mail já exista, uma IllegalArgumentException será lançada.
     *
     * @param usuario Objeto JSON contendo nome, e-mail, senha e demais dados opcionais.
     * @return Retorna o usuário criado ou uma mensagem de erro caso o e-mail já esteja em uso.
     */
    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastrar(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
            return ResponseEntity.ok(UsuarioMapper.toResponse(novoUsuario));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // =========================================================================================
    //  2. LOGIN DO USUÁRIO
    // =========================================================================================
    /**
     * Realiza a autenticação do usuário na plataforma.
     *
     * Valida:
     * - Se o e-mail existe no banco de dados;
     * - Se a senha informada corresponde à senha criptografada salva.
     *
     * Fluxo:
     * - Se autenticar com sucesso → retorna 200 com os dados do usuário;
     * - Se falhar → retorna erro HTTP 401 (Não autorizado).
     *
     * @param usuario Objeto contendo e-mail e senha informados no login.
     * @return Usuário autenticado ou mensagem "Email ou senha incorretos".
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        Optional<Usuario> usuarioLogado = usuarioService.login(usuario.getEmail(), usuario.getSenha());

        if (usuarioLogado.isPresent()) {
            return ResponseEntity.ok(UsuarioMapper.toResponse(usuarioLogado.get()));
        } else {
            return ResponseEntity.status(401).body("Email ou senha incorretos");
        }
    }

    // =========================================================================================
    //  3. BUSCAR USUÁRIO POR ID
    // =========================================================================================
    /**
     * Busca os dados de um usuário pelo seu ID no banco de dados.
     *
     * Retorno:
     * - 200 OK → caso o usuário seja encontrado;
     * - 404 Not Found → caso o ID não corresponda a nenhum usuário.
     *
     * @param id ID do usuário desejado.
     * @return Objeto Usuario ou erro 404 caso não encontrado.
     */
    @GetMapping("/buscarporid")
    public ResponseEntity<UsuarioResponseDTO> buscarPorId(@RequestParam Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }

    // =========================================================================================
    //  4. LISTAR TODOS OS USUÁRIOS
    // =========================================================================================
    /**
     * Lista todos os usuários cadastrados na plataforma.
     *
     * Retorno:
     * - 200 OK → lista de todos os usuários cadastrados.
     *
     * @return Lista de objetos Usuario.
     */
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodos() {
        System.out.println("=== ENDPOINT /api/usuarios CHAMADO ===");
        List<Usuario> usuarios = usuarioService.listarTodos();
        System.out.println("Total de usuários encontrados: " + usuarios.size());
        usuarios.forEach(u -> System.out.println("Usuário: ID=" + u.getId() + ", Nome=" + u.getNome() + ", Email=" + u.getEmail()));
        return ResponseEntity.ok(usuarios.stream().map(UsuarioMapper::toResponse).toList());
    }
}
