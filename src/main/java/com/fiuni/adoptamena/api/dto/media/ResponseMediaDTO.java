package com.fiuni.adoptamena.api.dto.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.Date;

import com.fiuni.adoptamena.api.dto.base.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMediaDTO extends BaseDTO {

    private String mimeType; // Tipo MIME (ej. "image/png", "video/mp4")

    private String url; // URL del archivo

    private Integer userId; // ID del usuario asociado al archivo

    private Date uploadDate; // Fecha de subida del archivo
}
