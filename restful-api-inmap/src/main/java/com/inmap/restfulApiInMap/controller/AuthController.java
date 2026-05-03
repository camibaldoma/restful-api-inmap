package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.classes.*;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        originPatterns = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*"
)
public class AuthController {
    @Autowired
    AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest)
    {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest)
    {
        return authService.register(registerRequest);
    }
    @PostMapping("/actualizarPassword")
    //Spring da el usuario del token automáticamente
    public ResponseEntity<AuthResponse> updatePassword(@RequestBody PasswordRequest passwordRequest, @AuthenticationPrincipal UserDetails currentUser) throws Exception
    {
        return ResponseEntity.ok(authService.updatePassword(passwordRequest,currentUser.getUsername()));
    }

}
