package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
