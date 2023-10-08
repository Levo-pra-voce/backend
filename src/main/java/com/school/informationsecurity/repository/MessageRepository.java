package com.school.informationsecurity.repository;

import com.school.informationsecurity.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
        SELECT m.message, m.sender.email, m.receiver.email, m.sender.firstName
            FROM Message m
                WHERE m.sender.email = :sender AND m.receiver.email = :receiver
        ORDER BY m.date
""")
    List<Message> getAllMessagesBySender(String sender, String receiver);
}
