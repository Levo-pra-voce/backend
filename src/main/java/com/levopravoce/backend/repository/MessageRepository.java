package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Group;
import com.levopravoce.backend.entities.Message;
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
              SELECT m.message, m.date, m.messageType, m.group.id
                  FROM Message m
                      WHERE m.group.id = :channelId
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
