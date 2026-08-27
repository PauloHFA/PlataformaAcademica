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
 * Controller REST responsável pelo gerenciamento de Usuários.
 * 
 * Camada: Presentation / REST Controller (Identity Context)
 * Contexto de Negócio: Cadastro, login, busca e gerenciamento de perfis de usuário.
 * Padrões aplicados: RestController, CrossOrigin, DTOs.
 * 
 * @see UsuarioService
 * @see docs/domain/identity_context.md
 * @see REQ-001 (Autenticação e Perfil de Usuário)
 */
@RestController
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
 * @see UsuarioService
 * @see docs/domain/identity_context.md
 * @see REQ-001 (Autenticação e Perfil de Usuário)
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
        System.out.println("[POST /api/usuarios/cadastro] Recebido: email=" + usuario.getEmail());
        try {
            Usuario novoUsuario = usuarioService.cadastrarUsuario(usuario);
            System.out.println("[POST /api/usuarios/cadastro] Sucesso: ID=" + novoUsuario.getId());
            return ResponseEntity.ok(UsuarioMapper.toResponse(novoUsuario));
        } catch (IllegalArgumentException e) {
            System.out.println("[POST /api/usuarios/cadastro] Erro: " + e.getMessage());
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
        System.out.println("[POST /api/usuarios/login] Tentativa: email=" + usuario.getEmail());
        Optional<Usuario> usuarioLogado = usuarioService.login(usuario.getEmail(), usuario.getSenha());

        if (usuarioLogado.isPresent()) {
            System.out.println("[POST /api/usuarios/login] Sucesso: ID=" + usuarioLogado.get().getId());
            return ResponseEntity.ok(UsuarioMapper.toResponse(usuarioLogado.get()));
        } else {
            System.out.println("[POST /api/usuarios/login] Falha: credenciais inválidas");
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
        System.out.println("[GET /api/usuarios/buscarporid] ID=" + id);
        Usuario usuario = usuarioService.buscarPorId(id);

        if (usuario == null) {
            System.out.println("[GET /api/usuarios/buscarporid] Não encontrado");
            return ResponseEntity.notFound().build();
        }

        System.out.println("[GET /api/usuarios/buscarporid] Retornando: " + usuario.getNome());
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
        System.out.println("[GET /api/usuarios] Listando todos");
        List<Usuario> usuarios = usuarioService.listarTodos();
        System.out.println("[GET /api/usuarios] Total: " + usuarios.size());
        return ResponseEntity.ok(usuarios.stream().map(UsuarioMapper::toResponse).toList());
    }
}
