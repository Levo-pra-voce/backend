package com.levopravoce.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "mensagem")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mensagem")
    private byte[] message;

    @Column(name = "ativo")
    private boolean active = false;

    @Column(name = "tipo_mensagem")
    @Enumerated(EnumType.STRING)
    private MESSAGE_TYPE messageType;

    @Column(name = "data_criacao")
    private LocalDateTime date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_mensagem_resposta")
    private Message responseMessage;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private User sender;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo")
    private Group group;

    enum MESSAGE_TYPE {
        TEXT,
        FILE
    }
}
