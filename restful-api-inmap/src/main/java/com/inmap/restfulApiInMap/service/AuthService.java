package com.inmap.restfulApiInMap.service;

import com.inmap.restfulApiInMap.classes.*;

public interface AuthService {
    public AuthResponse login(LoginRequest loginRequest);
    public RegisterResponse register(RegisterRequest registerRequest);
    public AuthResponse updatePassword(PasswordRequest passwordRequest, String token) throws Exception;
}
