package com.levopravoce.backend.entities;

import com.levopravoce.backend.services.chat.dto.ChatUserDTO;
import com.levopravoce.backend.services.chat.dto.MessageDTO;
import com.levopravoce.backend.services.chat.dto.MessageType;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SqlResultSetMapping;
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
@NamedNativeQuery(
    name = "Message.getChatListByCurrentUser",
    query = """
            select coalesce(sub.nome, u.nome) as nome, sub.id as channelId
              from (select g.id, g.nome
                      from usuario_grupo ug
                         join grupo g on g.id = ug.id_grupo
                    where ug.id_usuario = :userId) as sub
                       left join usuario_grupo ug on sub.nome is null and ug.id_grupo = sub.id
                       left join usuario u on u.id = ug.id_usuario
              where u.id is null or ug.id_usuario <> :userId
        """,
    resultSetMapping = "chatList")
@SqlResultSetMapping(
    name = "chatList",
    classes = @ConstructorResult(
        targetClass = ChatUserDTO.class,
        columns = {
            @ColumnResult(name = "nome", type = String.class),
            @ColumnResult(name = "channelId", type = Long.class)
        }
    )
)
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
