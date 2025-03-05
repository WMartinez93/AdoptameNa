package com.fiuni.adoptamena.auth;

import jakarta.validation.Valid;

import com.fiuni.adoptamena.exception_handler.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    @Autowired
    private final VerificationTokenService verificationTokenService;

    @Autowired
    private final PasswordResetService passwordResetTokenService;

    private final Boolean sendEmail = true;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse> register(
            @Valid @RequestBody RegisterRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        return ResponseEntity.ok(authService.register(request, sendEmail));
    }

    // Verificar el correo electrónico
    // TODO Cambiar a POST
    @PostMapping("/verify-email")
    public ResponseEntity<GenericResponse> verifyEmail(@RequestParam("token") String token) {
        return ResponseEntity.ok(verificationTokenService.verifyEmail(token));
    }

    // Generar token nuevamente
    @PostMapping("/resend-verification-token")
    public ResponseEntity<GenericResponse> resendVerificationToken(@RequestParam("email") String email) {
        return ResponseEntity.ok(verificationTokenService.sendVerificationEmail(email));
    }

    // Endpoint para solicitar el restablecimiento de contraseña
    @PostMapping("/reset-password-request")
    public ResponseEntity<GenericResponse> resetPasswordRequest(
            @RequestParam("email") String email) {
        // Enviar el correo con el enlace de restablecimiento de contraseña
        passwordResetTokenService.sendResetPasswordEmail(email);
        return ResponseEntity.ok(new GenericResponse("Si el correo existe, se ha enviado un enlace para restablecer la contraseña."));
    }

    // Endpoint para restablecer la contraseña
    @PostMapping("/reset-password")
    public ResponseEntity<GenericResponse> resetPassword(
            @RequestParam("token") String token,
            @RequestParam("newPassword") String newPassword) {
        passwordResetTokenService.resetPassword(token, newPassword);
        return ResponseEntity.ok(new GenericResponse("Contraseña restablecida exitosamente."));
    }
}
