package com.plataforma_academica.plataforma.repository;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório Spring Data JPA para a entidade Notificacao.
 * 
 * Camada: Persistence / Repository
 * Contexto de Negócio: Notificações do sistema para os usuários
 * Padrões aplicados: Spring Data JPA.
 * 
 * @see Notificacao
 */
@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, UUID> {
    List<Notificacao> findByUsuarioIdOrderByDataCriacaoDesc(UUID usuarioId);

    UUID countByUsuarioIdAndLida(UUID usuarioId, Boolean lida);
}
