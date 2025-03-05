package com.fiuni.adoptamena.api.dto.health_state;

import java.io.Serial;

import com.fiuni.adoptamena.api.dto.base.BaseDTO;
import lombok.*;
import jakarta.validation.constraints.*;

@Data
@EqualsAndHashCode(callSuper = false)
public class HealthStateDTO extends BaseDTO{
    @Serial
    private static final long serialVersionUID = 1L;

    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 500 caracteres")
    private String name;
}
