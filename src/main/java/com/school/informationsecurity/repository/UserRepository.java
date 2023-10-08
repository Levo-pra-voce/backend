package com.school.informationsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.school.informationsecurity.entities.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u.firstName
            FROM User u
                WHERE u.email = :email
    """)
    Optional<String> getNameByEmail(String email);
}
