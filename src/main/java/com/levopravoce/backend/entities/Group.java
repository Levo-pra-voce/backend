package com.levopravoce.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "grupo")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String name;

    @Column(name = "data_criacao")
    private LocalDateTime creationDate;

    @Column(name = "ativo")
    private boolean active = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_grupo",
            joinColumns = @JoinColumn(name = "id_grupo"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    private List<User> users;
}
