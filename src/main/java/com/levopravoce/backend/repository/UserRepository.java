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
      value = """
              SELECT EXISTS(SELECT 1 FROM usuario u WHERE u.email = :email)
    """, nativeQuery = true)
  boolean existsByEmail(String email);

  @Query(
      value = """
              SELECT EXISTS(SELECT 1 FROM usuario u WHERE u.email = :cpf)
    """, nativeQuery = true)
  boolean existsByCpf(String cpf);
}
