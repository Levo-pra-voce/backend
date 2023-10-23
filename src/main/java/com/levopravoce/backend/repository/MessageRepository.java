package com.levopravoce.backend.repository;

import com.levopravoce.backend.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
        SELECT m.message, m.sender.email, m.sender.firstName
            FROM Message m
                WHERE m.group.id = :groupId
        ORDER BY m.date
""")
    List<Message> getAllMessagesBySender(Long groupId);
}
