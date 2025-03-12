package com.fiuni.adoptamena.api.dto.base;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Data
public abstract class BaseDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // no se debe mandar el id en las peticiones
    private Integer id;
}
