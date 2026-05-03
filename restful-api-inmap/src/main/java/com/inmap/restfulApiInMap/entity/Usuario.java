package com.inmap.restfulApiInMap.entity;

import com.inmap.restfulApiInMap.classes.TieneAsociadoId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
// Esta anotación define contra que tabla de la base de datos la entidad se va a mapear
@Table(name = "usuarios")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario implements UserDetails {
    @Id
    @Column(name = "id_usuario")
    private String idUsuario;

    @Column(name = "nombre_usuario")
    private String username;

    @Column(name = "contrasena_usuario")
    private String password; // se guardará el hash de BCrypt

    @Column(name = "email_usuario", nullable = false, unique = true)
    private String email;

    @Column(name = "rol")
    private String rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
