package com.fiuni.adoptamena.api.controller.user;

import com.fiuni.adoptamena.api.dto.profile.ProfileDTO;
import com.fiuni.adoptamena.api.dto.user.UserDTO;
import com.fiuni.adoptamena.api.service.profile.IProfileService;
import com.fiuni.adoptamena.api.service.user.IUserService;
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
@RequestMapping("/users")
@Slf4j
@Tag(name = "User")
public class UserController {

    @Autowired
    private IProfileService profileService;

    @Autowired
    private IUserService userService;

    // Funciones de Usuario

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        return new ResponseEntity<>(userService.getAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Integer id) {
        UserDTO result = userService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@Valid @PathVariable Integer id, @RequestBody UserDTO user,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        user.setId(id);
        UserDTO result = userService.update(user);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Integer id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Funciones de Perfil

    @GetMapping("/profiles")
    public ResponseEntity<List<ProfileDTO>> getAllProfiles(Pageable pageable) {
        return new ResponseEntity<>(profileService.getAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<ProfileDTO> getProfileByUserId(@PathVariable Integer id) {
        ProfileDTO result = profileService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<ProfileDTO> updateProfile(@Valid @PathVariable Integer id, @RequestBody ProfileDTO profile,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }
        profile.setId(id);
        ProfileDTO result = profileService.update(profile);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
