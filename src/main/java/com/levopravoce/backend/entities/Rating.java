package com.levopravoce.backend.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "avaliacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_pedido")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "id_entregador")
    private User deliveryMan;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private User client;

    @Column(name = "nota")
    private Integer note;

    @Column(name = "comentario")
    private String comment;

    @Column(name = "data_criacao")
    private LocalDateTime creationDate;
}
