package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.entity.Materia;
import com.inmap.restfulApiInMap.entity.Password_reset_tokens;
import com.inmap.restfulApiInMap.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Password_reset_tokensRepository extends JpaRepository<Password_reset_tokens, String> {
    Optional<Password_reset_tokens> findByToken(String token);
}
