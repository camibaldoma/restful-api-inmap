package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.*;
import com.inmap.restfulApiInMap.entity.Usuario;
import com.inmap.restfulApiInMap.error.InvalidPasswordException;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.error.OverlapException;
import com.inmap.restfulApiInMap.jwt.JwtService;
import com.inmap.restfulApiInMap.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplementation implements AuthService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Override
    public AuthResponse login(LoginRequest loginRequest) throws NotFoundException,InvalidPasswordException {
        try
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
        } catch (BadCredentialsException e)
        {
            // Esto ocurre cuando el usuario existe pero la contraseña está mal
            throw new InvalidPasswordException("La contraseña ingresada no es correcta");
        }
        catch (Exception e)
        {
            // Spring suele lanzar InternalAuthenticationServiceException si el usuario no existe
            throw new NotFoundException("El nombre de usuario no existe en el sistema");
        }

        Usuario user = usuarioRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        String token = jwtService.jwtGetToken(user);
        return AuthResponse.builder()
                .token(token)
                .rol(user.getRol())
                .build();
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) throws OverlapException {
        if (usuarioRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            throw new OverlapException("El nombre de usuario '" + registerRequest.getUsername() + "' ya existe.");
        }
        Usuario usuario = new Usuario();
        String ultimoId = usuarioRepository.findLastId();
        String nuevoId;
        if (ultimoId == null) {
            nuevoId = "U1";
        } else {
            // se extrae el número después de la 'U'
            int numeroSiguiente = Integer.parseInt(ultimoId.substring(1)) + 1;
            nuevoId = "U" + numeroSiguiente;
        }
        usuario.setIdUsuario(nuevoId);
        usuario.setUsername(registerRequest.getUsername());
        usuario.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        usuario.setEmail(registerRequest.getEmail());
        usuario.setRol(registerRequest.getRole());


        usuarioRepository.save(usuario);

        return new RegisterResponse("Usuario " + usuario.getUsername() + " creado exitosamente.",usuario.getUsername());
    }

    @Override
    public AuthResponse updatePassword(PasswordRequest passwordRequest,String username) throws InvalidPasswordException,NotFoundException{
        //Si llegó hasta acá es porque se validó correctamente el token enviado en la petición
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(()-> new NotFoundException("Usuario no encontrado"));
        //Reautenticación para operaciones sesibles. Se pide la contraseña y se valida antes de actualizar
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), usuario.getPassword())) {
            throw new InvalidPasswordException("La clave ingresada actual no es correcta");
        }
        usuario.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        usuarioRepository.save(usuario);
        //Se devuelve el token nuevo
        return AuthResponse.builder()
                .token(jwtService.jwtGetToken(usuario))
                .rol(usuario.getRol())
                .build();
    }
}
