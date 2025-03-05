package com.fiuni.adoptamena.api.dto.breed;

import java.io.Serial;

import com.fiuni.adoptamena.api.dto.base.BaseDTO;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BreedDTO extends BaseDTO{
    @Serial
    private static final long serialVersionUID = 1L;

    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String name;
    
    private Integer animalId;
}
