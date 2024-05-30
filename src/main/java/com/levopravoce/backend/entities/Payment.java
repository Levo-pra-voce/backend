package com.levopravoce.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "pagamento")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor")
    private Float value;

    @Column(name = "data_criacao")
    private LocalDateTime creationDate;

    @Column(name = "ativo")
    private Boolean active = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_entregador")
    private User userReceiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private User userPayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_veiculo")
    private Vehicle vehicle;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public enum PaymentStatus {
        PENDENTE, PAGO, CANCELADO
    }
}
