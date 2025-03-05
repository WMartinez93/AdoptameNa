package com.fiuni.adoptamena.api.dto.pet;

import com.fiuni.adoptamena.api.domain.profile.EnumGender;
import com.fiuni.adoptamena.api.dto.base.BaseDTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.util.Date;
@Data
@EqualsAndHashCode(callSuper = false)
public class PetDTO extends BaseDTO{
    @Serial
    private static final long serialVersionUID = 1L;

    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    @NotBlank
    private String name;

    private Boolean isVaccinated;

    @Size(min = 1, max = 500, message = "La descripcion debe tener entre 1 y 500 caracteres")
    private String description;

    private Date birthdate;

    @Pattern(regexp = "^(MALE|FEMALE|OTHER)$", message = "El genero debe ser MALE, FEMALE u OTHER")
    private EnumGender gender;

    private String urlPhoto;

    private Boolean isSterilized;

    private Integer userId;

    private Integer animalId;

    private Integer breedId;

    private Integer healthStateId;

    private Integer petStatusId;
}
