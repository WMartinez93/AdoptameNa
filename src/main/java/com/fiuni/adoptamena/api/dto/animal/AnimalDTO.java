package com.fiuni.adoptamena.api.dto.animal;

import java.io.Serial;

import com.fiuni.adoptamena.api.dto.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.*;


@Data
@EqualsAndHashCode(callSuper = false)
public class AnimalDTO extends BaseDTO{
    @Serial
    private static final long serialVersionUID = 1L;
    
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @NotBlank
    private String name;
}
