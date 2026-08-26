package com.plataforma_academica.plataforma.sharedkernel.domain.model.identifier;

import java.util.UUID;

/**
 * Interface marcadora (Marker Interface) para todos os identificadores únicos
 * (Value Objects de ID) no sistema.
 * 
 * Garante que qualquer Value Object que represente um ID implemente esta
 * interface,
 * permitindo polimorfismo e padronização no acesso ao valor primitivo
 * (UUID/Long).
 */
public interface Identificador {
    /**
     * Retorna o valor primitivo subjacente do identificador.
     * 
     * @return O valor do ID (ex: UUID ou Long).
     */
    UUID valor();
}
