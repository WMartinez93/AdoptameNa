package com.fiuni.adoptamena.api.dto.post;

import com.fiuni.adoptamena.api.dto.base.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor

public class PostDTO extends BaseDTO {

    @NotNull
    private Integer idUser;

    @NotBlank(message = "El titulo es Obligatorio.")
    @Size(min =5, max= 255, message = "El titulo debe tener como minimo 5 caracteres.")
    private String title;

    private String content;

    @NotNull
    private Integer idPostType;

    private String locationCoordinates;

    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only numbers")
    private String contactNumber;

    private String status;

    private int sharedCounter;

    private Date publicationDate;

}
