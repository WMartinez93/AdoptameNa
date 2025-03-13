package com.fiuni.adoptamena.api.controller.media;

import com.fiuni.adoptamena.api.dto.media.RequestMediaDTO;
import com.fiuni.adoptamena.api.dto.media.ResponseMediaDTO;
import com.fiuni.adoptamena.api.service.media.IMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/media")
public class MediaController {

    @Autowired
    private IMediaService mediaService;

    // Obtener un archivo media por ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMediaDTO> getById(@PathVariable Integer id) {
        ResponseMediaDTO media = mediaService.getById(id);
        return ResponseEntity.ok(media);
    }

    // Obtener todos los archivos media con paginación
    // No se utiliza en el front-end (Permanece para propósitos de debugging)
    @GetMapping("/")
    public ResponseEntity<List<ResponseMediaDTO>> getAll(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        List<ResponseMediaDTO> mediaList = mediaService.getAll(PageRequest.of(page, size));
        return ResponseEntity.ok(mediaList);
    }

    // Subir un nuevo archivo media
    @PostMapping("/upload")
    public ResponseEntity<ResponseMediaDTO> uploadMedia(@RequestBody RequestMediaDTO mediaDto) {
        ResponseMediaDTO media = mediaService.uploadMedia(mediaDto);
        return ResponseEntity.ok(media);
    }

    // Eliminar un archivo media
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Integer id) {
        mediaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}