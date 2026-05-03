package com.inmap.restfulApiInMap.repository;

import com.inmap.restfulApiInMap.dto.DestinoReducidoDTO;
import com.inmap.restfulApiInMap.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    Optional<Usuario> findByUsername(String username);
    // Esta consulta busca el ID más alto
    @Query("SELECT u.idUsuario FROM Usuario u WHERE u.idUsuario LIKE 'U%' ORDER BY CAST(SUBSTRING(u.idUsuario, 2) AS int) DESC LIMIT 1")
    String findLastId();

    Optional<Usuario> findByEmail(String email);
}
