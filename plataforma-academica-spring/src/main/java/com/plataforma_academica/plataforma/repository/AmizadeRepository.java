package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Amizade;
import com.plataforma_academica.plataforma.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para a entidade Amizade.
 * 
 * Responsável por realizar operações de persistência e consultas customizadas
 * (JPQL)
 * para gerenciar o relacionamento entre usuários (amizades, solicitações
 * pendentes, etc.).
 * 
 * Camada: Infrastructure / Persistence Repository
 * 
 * @see Amizade
 * @see REQ-020 (Gestão de Conexões e Amizades)
 */
@Repository
public interface AmizadeRepository extends JpaRepository<Amizade, Long> {

        /**
         * Busca amizades de um usuário específico filtradas por status.
         * 
         * @param user   Usuário alvo.
         * @param status Status da amizade (PENDENTE, ACEITO, etc.).
         * @return Lista de amizades correspondentes.
         */
        @Query("SELECT a FROM Amizade a " +
                        "WHERE (a.solicitante = :user OR a.destinatario = :user) " +
                        "AND a.status = :status")
        List<Amizade> findAmizades(@Param("user") Usuario user,
                        @Param("status") Amizade.Status status);

        /**
         * Busca uma solicitação de amizade específica entre dois IDs de usuários.
         * 
         * @param solicitanteId  ID do usuário remetente.
         * @param destinatarioId ID do usuário destinatário.
         * @return Optional contendo a amizade se encontrada.
         */
        Optional<Amizade> findBySolicitanteIdAndDestinatarioId(Long solicitanteId, Long destinatarioId);

        /**
         * Busca todas as conexões (como solicitante ou destinatário) de um usuário.
         * 
         * @param id1 ID do primeiro parâmetro de busca.
         * @param id2 ID do segundo parâmetro de busca (geralmente igual ao id1).
         * @return Lista de conexões.
         */
        List<Amizade> findBySolicitanteIdOrDestinatarioId(Long id1, Long id2);

        List<Amizade> findBySolicitanteOrDestinatarioAndStatus(Usuario usuario, Usuario usuario1,
                        Amizade.Status status);

        /**
         * Busca todos os amigos aceitos de um usuário através do ID.
         * 
         * @param usuarioId ID do usuário.
         * @return Lista de amizades com status ACEITO.
         */
        @Query("SELECT a FROM Amizade a WHERE (a.solicitante.id = :usuarioId OR a.destinatario.id = :usuarioId) AND a.status = 'ACEITO'")
        List<Amizade> findAmigosAceitos(@Param("usuarioId") Long usuarioId);

        @Query("SELECT a FROM Amizade a WHERE (a.solicitante.id = :usuarioId OR a.destinatario.id = :usuarioId) AND a.status = 'PENDENTE'")
        List<Amizade> findSolicitacoesPendentes(@Param("usuarioId") Long usuarioId);
}
