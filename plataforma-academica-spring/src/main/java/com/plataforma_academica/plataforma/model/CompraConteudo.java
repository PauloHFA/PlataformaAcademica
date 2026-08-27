/**
 * Entidade JPA que representa uma compra de conteúdo no mercado acadêmico.
 * 
 * Camada: Persistence / Domain Entity (Commercial Context)
 * Contexto de Negócio: Registro de transação entre usuário e conteúdo,
 * rastreando data, valor e status de pagamento.
 * 
 * @see ConteudoMercado
 * @see Usuario
 * @see REQ-051 (Compras no Mercado Acadêmico)
 */
package com.plataforma_academica.plataforma.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "compra_conteudo")
public class CompraConteudo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conteudo_mercado_id", nullable = false)
    private ConteudoMercado conteudoMercado;

    private LocalDateTime dataCompra = LocalDateTime.now();
    private Double valorPago;
    private Boolean pago = false;
}
