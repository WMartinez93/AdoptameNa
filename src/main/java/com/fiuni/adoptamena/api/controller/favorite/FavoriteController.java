package com.fiuni.adoptamena.api.controller.favorite;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fiuni.adoptamena.api.dto.favorite.FavoriteDTO;
import com.fiuni.adoptamena.api.service.favorite.FavoriteServiceImp;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/favorites")
@Tag(name = "Favorites")
public class FavoriteController {

    @Autowired
    private FavoriteServiceImp favoriteService;

    /**
     * Crea un nuevo favorito para el usuario autenticado
     */
    @PostMapping
    public ResponseEntity<FavoriteDTO> createFavorite(@RequestBody FavoriteDTO dto) {

        FavoriteDTO created = favoriteService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Obtiene un favorito específico por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<FavoriteDTO> getFavoriteById(@PathVariable Integer id) {

        FavoriteDTO favorite = favoriteService.getById(id);

        // Verificar si el favorito pertenece al usuario autenticado
        if (favorite == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(favorite);
    }

    /**
     * Obtiene todos los favoritos (paginados) para el admin
     * Decidir como usar los endpoints de admin luego
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<FavoriteDTO>> getAllFavorites(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<FavoriteDTO> favorites = favoriteService.getAll(pageable);

        return ResponseEntity.ok(favorites);
    }

    @GetMapping
    public ResponseEntity<List<FavoriteDTO>> getAllFavoritesByUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<FavoriteDTO> favorites = favoriteService.getAllByUserId(pageable);

        return ResponseEntity.ok(favorites);
    }

    /**
     * Elimina un favorito por ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Integer id) {

        favoriteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}