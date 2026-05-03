package com.inmap.restfulApiInMap.controller;

import com.inmap.restfulApiInMap.classes.ResetPasswordRequest;
import com.inmap.restfulApiInMap.error.NotFoundException;
import com.inmap.restfulApiInMap.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        originPatterns = "*",
        allowCredentials = "true",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS},
        allowedHeaders = "*"
)
public class EmailController {
    @Autowired
    private EmailService emailService;
    @PostMapping("/recuperarContraseña")
    public void recoveredPassword(@RequestParam String data) {
        emailService.requestPasswordReset(data);
    }
    @PostMapping("/nuevaContraseña")
    public ResponseEntity<?> newPassword(@RequestBody ResetPasswordRequest request) {
            emailService.newPasswordReset(request);
            return ResponseEntity.ok("Contraseña actualizada con éxito.");

    }
}
