package com.levopravoce.backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "solicitacao")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entregador")
    private User deliveryman;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    public enum RequestStatus {
        SOLICITADO, ACEITO, RECUSADO
    }
}
