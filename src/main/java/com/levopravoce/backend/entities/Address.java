package com.levopravoce.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "endereco")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cep")
    private String zipCode;

    @Column(name = "logradouro")
    private String street;

    @Column(name = "numero")
    private String number;

    @Column(name = "complemento")
    private String complement;

    @Column(name = "bairro")
    private String neighborhood;

    @Column(name = "cidade")
    private String city;

    @Column(name = "estado")
    private String state;

    @Column(name = "data_criacao")
    private LocalDateTime creationDate;

    @Column(name = "ativo")
    private boolean active = false;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private User user;
}
