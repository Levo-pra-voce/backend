package com.levopravoce.backend.repository;

import java.util.Optional;

import com.levopravoce.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
  @Query(
      value = """
              select * from usuario u where u.email = :email and u.status = 'ACTIVE'
          """, nativeQuery = true)
  Optional<User> findByEmail(String email);

  @Query(
      """
              SELECT u.name
            FROM User u
                WHERE u.email = :email and u.status = com.levopravoce.backend.entities.Status.ACTIVE
    """)
  Optional<String> getNameByEmail(String email);

  boolean existsByEmail(String email);

  boolean existsByCpf(String cpf);
}
