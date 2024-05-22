package com.levopravoce.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "altura")
    private double height;

    @Column(name = "largura")
    private double width;

    @Column(name = "peso_maximo")
    private double maxWeight;

    @Column(name = "data_entrega")
    private LocalDate deliveryDate;

    @Column(name = "seguro")
    private Boolean haveSecurity;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "valor")
    private double value;

    @ManyToOne
    @JoinColumn(name = "id_entregador")
    private User deliveryman;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private User client;

    @ManyToOne
    @JoinColumn(name = "id_veiculo")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "id_pagamento")
    private Payment payment;

    public enum OrderStatus {
        PENDING, IN_PROGRESS, DELIVERED, CANCELED
    }
}
