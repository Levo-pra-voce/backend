package com.levopravoce.backend.entities;

import com.levopravoce.backend.services.chat.dto.MessageDTO;
import com.levopravoce.backend.services.chat.dto.MessageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public MessageDTO toMessageDTO() {

        String message = switch (this.messageType) {
            case TEXT -> new String(this.message);
            case IMAGE -> {
                Base64.Encoder encoder = Base64.getEncoder();
                yield encoder.encodeToString(this.message);
            }
        };

        return MessageDTO.builder()
            .message(message)
            .sender(this.sender.getEmail())
            .channelId(this.group.getId())
            .timestamp(this.date.atZone(ZoneId.of("UTC")).toEpochSecond())
            .type(this.messageType)
            .build();
    }
}
