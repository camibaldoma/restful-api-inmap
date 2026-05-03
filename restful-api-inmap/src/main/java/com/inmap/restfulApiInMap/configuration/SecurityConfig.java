package com.inmap.restfulApiInMap.configuration;

import com.inmap.restfulApiInMap.jwt.jwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final jwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF para que funcionen los POST/PUT de la API
                .cors(Customizer.withDefaults()) // Usar la configuración de CORS que ya se hizo
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/**").permitAll() // los GETs se liberan
                        .requestMatchers(HttpMethod.POST, "/login").permitAll() // los métodos para loguearse también
                        .requestMatchers(HttpMethod.POST, "/reportWifi").permitAll()
                        .requestMatchers(HttpMethod.POST, "/recuperarContraseña").permitAll()
                        .requestMatchers(HttpMethod.POST, "/nuevaContraseña").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/eliminarUsuario/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/actualizarPassword/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/register").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/obtenerReporte").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
