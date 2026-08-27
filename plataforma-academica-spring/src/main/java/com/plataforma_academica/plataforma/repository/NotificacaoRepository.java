package com.plataforma_academica.plataforma.repository;

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
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByUsuarioIdOrderByDataCriacaoDesc(Long usuarioId);

    Long countByUsuarioIdAndLida(Long usuarioId, Boolean lida);
}
