package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Group;
import com.levopravoce.backend.entities.Message;
import com.levopravoce.backend.services.chat.dto.ChatUserDTO;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

  @Query(value = """
              SELECT distinct ug.id_usuario
                  FROM public.usuario_grupo ug
              WHERE ug.id_grupo = :groupId
      """, nativeQuery = true)
  List<Long> getUsersIdsByGroup(Long groupId);

  @Query(
      value = """
              SELECT true
                    FROM public.usuario_grupo m
              WHERE m.id_grupo = :groupId and m.id_usuario = :userId
          """,
      nativeQuery = true
  )
  boolean haveAccessForGroup(Long groupId, Long userId);

  @Query(
      value = """
              SELECT m, g
                  FROM Message m
                        JOIN Group g on g.id = m.group.id
                    WHERE m.group.id = :channelId and g.active = true
               ORDER BY m.date
          """
  )
  List<Message> getAllByChannelId(Long channelId);

  @Query(
      value = """
              SELECT m, g
                  FROM Message m
                        JOIN Group g on g.id = m.group.id
                    WHERE m.group.id = :channelId and g.active = true and m.date > :lastDate
               ORDER BY m.date
          """
  )
  List<Message> getAllByChannelIdAndTimestamp(Long channelId, LocalDateTime lastDate);

  @Query(
      value = """
              SELECT g
                  FROM Group g
                      WHERE g.id = :groupId and g.active = true
          """
  )
  Group getGroupById(Long groupId);

  @Query(nativeQuery = true)
  List<ChatUserDTO> getChatListByCurrentUser(Long userId);
}
