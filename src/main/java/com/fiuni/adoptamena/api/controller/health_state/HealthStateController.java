package com.fiuni.adoptamena.api.controller.health_state;

import com.fiuni.adoptamena.api.dto.health_state.HealthStateDTO;
import com.fiuni.adoptamena.api.service.health_state.IHealthStateService;
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
@RequestMapping("/health-states")
@Slf4j
@Tag(name = "Health State")
public class HealthStateController {

    @Autowired
    private IHealthStateService healthStateService;

    @GetMapping("/{id}")
    public ResponseEntity<HealthStateDTO> get(@PathVariable Integer id) {
        HealthStateDTO result = healthStateService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HealthStateDTO> create(@Valid @RequestBody HealthStateDTO healthState, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        HealthStateDTO result = healthStateService.create(healthState);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HealthStateDTO> update(@Valid @PathVariable Integer id, @RequestBody HealthStateDTO healthState,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        healthState.setId(id);
        HealthStateDTO result = healthStateService.update(healthState);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        healthStateService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<HealthStateDTO>> getAll(Pageable pageable) {
        return new ResponseEntity<>(healthStateService.getAll(pageable), HttpStatus.OK);
    }
}
