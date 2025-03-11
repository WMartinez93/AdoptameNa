package com.fiuni.adoptamena.api.controller.breed;

import com.fiuni.adoptamena.api.dto.breed.BreedDTO;
import com.fiuni.adoptamena.api.service.breed.IBreedService;
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
@RequestMapping("/breed")
@Slf4j
@Tag(name = "Breed")
public class BreedController {

    @Autowired
    private IBreedService breedService;

    @GetMapping("/{id}")
    public ResponseEntity<BreedDTO> get(@PathVariable Integer id) {
        BreedDTO result = breedService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BreedDTO> create(@Valid @RequestBody BreedDTO breed, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        BreedDTO result = breedService.create(breed);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BreedDTO> update(@Valid @PathVariable Integer id, @RequestBody BreedDTO breed,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        breed.setId(id);
        BreedDTO result = breedService.update(breed);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        breedService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BreedDTO>> getAll(Pageable pageable) {
        return new ResponseEntity<>(breedService.getAll(pageable), HttpStatus.OK);
    }
}
 