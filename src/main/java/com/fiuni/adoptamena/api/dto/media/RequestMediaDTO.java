package com.fiuni.adoptamena.api.dto.media;

import lombok.*;

import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestMediaDTO {
    
    private MultipartFile file;  // El archivo a subir
}
