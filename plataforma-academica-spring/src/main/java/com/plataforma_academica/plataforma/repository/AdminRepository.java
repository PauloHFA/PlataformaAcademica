package com.plataforma_academica.plataforma.repository;
import java.util.UUID;

import com.plataforma_academica.plataforma.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositório JPA para a entidade {@link Admin}.
 *
 * Camada: Persistence / Repository Pattern (Identity Context)
 * Operações CRUD para contas administrativas.
 *
 * @see com.plataforma_academica.plataforma.model.Admin
 * @see REQ-099 (Administração da Plataforma)
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {
}