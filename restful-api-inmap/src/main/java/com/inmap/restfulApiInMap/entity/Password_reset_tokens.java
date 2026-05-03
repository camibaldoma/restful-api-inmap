package com.inmap.restfulApiInMap.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
// Esta anotación define contra que tabla de la base de datos la entidad se va a mapear
@Table(name = "password_reset_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Password_reset_tokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Si se usa SERIAL en SQL
    @Column(name = "id_token")
    private Long idToken;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    // Relación 1 a 1 con la entidad Usuario
    @OneToOne
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;


    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.fechaExpiracion);
    }


}
