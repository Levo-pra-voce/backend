package com.levo_pra_voce.backend.repository;

import java.util.Optional;

import com.levo_pra_voce.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
        SELECT u.email, u.userType
            FROM User u
                WHERE u.email = :email
    """)
    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u.firstName
            FROM User u
                WHERE u.email = :email
    """)
    Optional<String> getNameByEmail(String email);
}
