package com.fiuni.adoptamena.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Formato de email incorrecto")
    @Schema(example = "email@example.com")
    String email;

    @NotBlank(message = "La contrasena no puede estar vacio")
    @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
    @Schema(example = "ContraseñaSegura123.")
    String password;
}
