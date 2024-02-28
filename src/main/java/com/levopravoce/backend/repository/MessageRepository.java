package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Group;
import com.levopravoce.backend.entities.Message;
import com.levopravoce.backend.services.chat.dto.MessageDTO;
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
              SELECT
                  EXISTS (
                          SELECT 1
                              FROM public.usuario_grupo m
                          WHERE m.id_grupo = :groupId and m.id_usuario = :userId
                      )
          """,
      nativeQuery = true
  )
  boolean haveAccessForGroup(Long groupId, Long userId);

  @Query(
      value = """
              SELECT m, g
                  FROM Message m
                        JOIN Group g on g.id = m.sender.id
                    WHERE m.sender.id = :channelId
          """
  )
  List<Message> getAllByChannelId(Long channelId);

  @Query(
      value = """
              SELECT g
                  FROM Group g
                      WHERE g.id = :groupId
          """
  )
  Group getGroupById(Long groupId);
}
