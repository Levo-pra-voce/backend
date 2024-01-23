package com.levopravoce.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "veiculo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    @JsonIgnore
    private User user;

    @Column(name = "placa")
    private String plate;

    @Column(name = "modelo")
    private String model;

    @Column(name = "cor")
    private String color;

    @Column(name = "data_criacao")
    private LocalDateTime creationDate;

    @Column(name = "ativo")
    private boolean active = false;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_veiculo")
    private VehicleType vehicleType;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_veiculo")
    private List<Rating> ratings;

}
