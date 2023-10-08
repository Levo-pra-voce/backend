package com.school.informationsecurity.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avaliacao")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_veiculo")
    private Vehicle vehicle;

    @Column(name = "nota")
    private Integer note;

    @Column(name = "comentario")
    private String comment;

    @Column(name = "ativo")
    private boolean active = false;

    @Column(name = "data_criacao")
    private String creationDate;
}
