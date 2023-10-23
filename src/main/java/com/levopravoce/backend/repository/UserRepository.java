package com.levopravoce.backend.repository;

import java.util.Optional;

import com.levopravoce.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
  @Query(
      """
        SELECT u.email, u.userType
            FROM User u
                WHERE u.email = :email and u.status = com.levopravoce.backend.entities.Status.ACTIVE
    """)
  Optional<User> findByEmail(String email);

  @Query(
      """
        SELECT u.firstName
            FROM User u
                WHERE u.email = :email and u.status = com.levopravoce.backend.entities.Status.ACTIVE
    """)
  Optional<String> getNameByEmail(String email);

  boolean existsByEmailOrCpf(String email, String cpf);
}
