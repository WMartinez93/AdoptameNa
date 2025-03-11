package com.fiuni.adoptamena.api.dto.post;

import com.fiuni.adoptamena.api.dto.base.BaseDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class PostReportDto extends BaseDTO {

    @NotNull(message = "El usuario debe ser Obligatorio.")
    private Integer idUser;

    @NotNull(message = "El post debe ser Obligatorio.")
    private Integer idPost;

    @NotNull(message = "La razon del reporte debe ser Obligatorio.")
    private Integer idReportReason;

    private String description;

    private Date reportDate;

    private String status;
}