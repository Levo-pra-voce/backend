package com.levopravoce.backend.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Double height;

    @Column(name = "largura")
    private Double width;

    @Column(name = "peso_maximo")
    private Double maxWeight;

    @Column(name = "data_entrega")
    private LocalDateTime deliveryDate;

    @Column(name = "seguro")
    private Boolean haveSecurity;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "valor")
    private Double value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_entregador")
    private User deliveryman;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    private User client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_veiculo")
    private Vehicle vehicle;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pedido")
    private List<Request> requests;

    @Column(name = "origem_latitude")
    private Double originLatitude;

    @Column(name = "origem_longitude")
    private Double originLongitude;

    @Column(name = "origem_endereco")
    private String originAddress;

    @Column(name = "destino_latitude")
    private Double destinationLatitude;

    @Column(name = "destino_longitude")
    private Double destinationLongitude;

    @Column(name = "destino_endereco")
    private String destinationAddress;

    @Column(name = "distancia_metros")
    private Long distanceMeters;

    @Column(name = "duracao_segundos")
    private Long durationSeconds;

    public enum OrderStatus {
        CANCELADO, ESPERANDO, ACEITO, EM_PROGRESSO, ENTREGADO, FEITO_PAGAMENTO
    }
}
