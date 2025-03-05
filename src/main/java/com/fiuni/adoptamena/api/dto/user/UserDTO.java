package com.fiuni.adoptamena.api.dto.user;

import java.util.Date;

import com.fiuni.adoptamena.api.dto.base.BaseDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserDTO extends BaseDTO {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El email no es válido")
    private String email;
    private Date creationDate;
    private String role;
    private Boolean isVerified;

}
