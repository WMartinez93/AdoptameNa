package com.fiuni.adoptamena.api.controller.pet;

import com.fiuni.adoptamena.api.dto.pet.PetDTO;
import com.fiuni.adoptamena.api.service.pet.IPetService;
import com.fiuni.adoptamena.exception_handler.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private IPetService petService;

    // Obtener todas las mascotas (con paginación)
    @GetMapping
    public ResponseEntity<List<PetDTO>> getAllPets(Pageable pageable) {
        List<PetDTO> pets = petService.getAll(pageable);
        return ResponseEntity.ok(pets);
    }

    // Obtener una mascota por ID
    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPetById(@PathVariable Integer id) {
        try {
            PetDTO pet = petService.getById(id);
            return ResponseEntity.ok(pet);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Crear una nueva mascota
    @PostMapping
    public ResponseEntity<PetDTO> createPet(@RequestBody PetDTO petDTO) {
        PetDTO createdPet = petService.create(petDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    // Actualizar una mascota
    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> updatePet(@PathVariable Integer id, @RequestBody PetDTO petDTO) {
        try {
            petDTO.setId(id);
            PetDTO updatedPet = petService.update(petDTO);
            return ResponseEntity.ok(updatedPet);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Eliminar (borrado lógico) una mascota
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Integer id) {
        try {
            petService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
