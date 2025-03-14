package com.fiuni.adoptamena.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Size(min = 3, max = 100, message = "El nombre de la organización debe tener entre 3 y 50 caracteres")
    String organizationName;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 50 caracteres")
    String fullName;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Formato de email incorrecto")
    String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
    String password;

    @NotBlank(message = "El rol no puede estar vacío")
    private String role;
}
