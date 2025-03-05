package com.fiuni.adoptamena.api.controller.animal;

import com.fiuni.adoptamena.api.dto.animal.AnimalDTO;
import com.fiuni.adoptamena.api.service.animal.IAnimalService;
import com.fiuni.adoptamena.exception_handler.exceptions.BadRequestException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RestController
@RequestMapping("/animals")
@Slf4j
@Tag(name = "Animal")
public class AnimalController {

    @Autowired
    private IAnimalService animalService;

    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> get(@PathVariable Integer id) {
        AnimalDTO result = animalService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AnimalDTO> create(@Valid @RequestBody AnimalDTO animal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        AnimalDTO result = animalService.create(animal);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalDTO> update(@Valid @PathVariable Integer id, @RequestBody AnimalDTO animal,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        animal.setId(id);
        AnimalDTO result = animalService.update(animal);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        animalService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AnimalDTO>> getAll(Pageable pageable) {
        return new ResponseEntity<>(animalService.getAll(pageable), HttpStatus.OK);
    }
}
 