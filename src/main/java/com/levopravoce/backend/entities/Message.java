package com.levopravoce.backend.entities;

import com.levopravoce.backend.services.chat.dto.MessageResponseDTO;
import com.levopravoce.backend.services.chat.dto.MessageType;
import jakarta.persistence.*;
import java.util.Base64;
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
    private MessageType messageType;

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

    public MessageResponseDTO toMessageResponseDTO() {

        String message = switch (this.messageType) {
            case TEXT -> new String(this.message);
            case IMAGE -> {
                Base64.Encoder encoder = Base64.getEncoder();
                yield encoder.encodeToString(this.message);
            }
        };

        return MessageResponseDTO.builder()
            .message(message)
            .sender(this.sender.getEmail())
            .channelId(this.group.getId())
            .type(this.messageType)
            .build();
    }
}
