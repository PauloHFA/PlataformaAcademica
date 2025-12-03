package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
    @Query("SELECT m FROM Mensagem m WHERE (m.remetenteId = :usuarioId AND m.destinatarioId = :amigoId) OR (m.remetenteId = :amigoId AND m.destinatarioId = :usuarioId) ORDER BY m.criadoEm ASC")
    List<Mensagem> findMensagensEntre(Long usuarioId, Long amigoId);
}
