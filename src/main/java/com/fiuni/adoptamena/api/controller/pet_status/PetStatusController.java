package com.fiuni.adoptamena.api.controller.pet_status;

import com.fiuni.adoptamena.api.dto.pet_status.PetStatusDTO;
import com.fiuni.adoptamena.api.service.pet_status.IPetStatusService;
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
@RequestMapping("/pet-status")
@Slf4j
@Tag(name = "Pet Status")
public class PetStatusController {

    @Autowired
    private IPetStatusService petStatusService;

    @GetMapping("/{id}")
    public ResponseEntity<PetStatusDTO> get(@PathVariable Integer id) {
        PetStatusDTO result = petStatusService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PetStatusDTO> create(@Valid @RequestBody PetStatusDTO petStatus, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        PetStatusDTO result = petStatusService.create(petStatus);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetStatusDTO> update(@Valid @PathVariable Integer id, @RequestBody PetStatusDTO petStatus,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        petStatus.setId(id); // Asegura que se actualice el estado correcto
        PetStatusDTO result = petStatusService.update(petStatus);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        petStatusService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<PetStatusDTO>> getAll(Pageable pageable) {
        return new ResponseEntity<>(petStatusService.getAll(pageable), HttpStatus.OK);
    }
}
