package com.plataforma_academica.plataforma.identity.domain.model;

/**
 * Enumeração dos papéis/perfis de acesso no sistema (RBAC - Role-Based Access
 * Control).
 * 
 * Define as permissões baseadas em função para controle de acesso aos recursos
 * da plataforma.
 * Segue o padrão Spring Security "ROLE_" prefix para integração nativa
 * com @PreAuthorize.
 * 
 * Hierarquia implícita: ADMIN > PROFESSOR > ALUNO
 */
public enum Papel {
    /**
     * Estudante da plataforma. Acesso a salas de aula, atividades, submissões e
     * perfil próprio.
     */
    ROLE_ALUNO,

    /**
     * Docente da plataforma. Acesso a gestão de salas, criação de atividades,
     * correção de submissões,
     * gestão de membros da sala e relatórios de desempenho.
     */
    ROLE_PROFESSOR,

    /**
     * Administrador do sistema. Acesso total a todos os recursos, gestão de
     * usuários,
     * configurações globais, auditoria e métricas.
     */
    ROLE_ADMIN
}