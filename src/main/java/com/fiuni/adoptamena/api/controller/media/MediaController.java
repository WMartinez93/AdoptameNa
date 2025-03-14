package com.fiuni.adoptamena.api.controller.media;

import com.fiuni.adoptamena.api.dto.media.MediaDTO;
import com.fiuni.adoptamena.api.service.media.IMediaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/media")
@Tag(name = "Media")
public class MediaController {

    @Autowired
    private IMediaService mediaService;

    // Obtener un archivo media por ID
    @GetMapping("/{id}")
    public ResponseEntity<MediaDTO> getById(@PathVariable Integer id) {
        MediaDTO media = mediaService.getById(id);
        return ResponseEntity.ok(media);
    }

    // Obtener todos los archivos media con paginación
    // No se utiliza en el front-end (Permanece para propósitos de debugging)
    @GetMapping("/")
    public ResponseEntity<List<MediaDTO>> getAll(Pageable pageable) {
        List<MediaDTO> mediaList = mediaService.getAll(pageable);
        return ResponseEntity.ok(mediaList);
    }

    @PostMapping("/upload")
    @Operation(summary = "Subir un archivo multimedia", description = "Este endpoint permite subir archivos multimedia, principalmente utilizado para cargar y usar fotos. Se recomienda no cargar archivos de gran tamaño, como fotos de alta resolución o videos. Aunque el endpoint aún está en desarrollo, se ha habilitado para permitir que el front-end comience a subir archivos multimedia al sistema.")
    public ResponseEntity<MediaDTO> uploadMedia(
            @RequestBody(content = @Content(mediaType = "multipart/form-data", schema = @Schema(type = "string", format = "binary"))) @RequestParam("file") MultipartFile file) {

        MediaDTO media = mediaService.uploadMedia(file);
        return ResponseEntity.ok(media);
    }

    // Eliminar un archivo media
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Integer id) {
        mediaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}