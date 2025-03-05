package com.fiuni.adoptamena.api.controller.post;

import com.fiuni.adoptamena.api.dto.post.PostTypeDTO;
import com.fiuni.adoptamena.api.service.post.IPostTypeService;
import com.fiuni.adoptamena.exception_handler.exceptions.BadRequestException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/postTypes")
@Tag(name = "PostType")
public class PostTypeController {

    @Autowired
    private IPostTypeService postTypeService;

    @GetMapping("/{id}")
    public ResponseEntity<PostTypeDTO> getPostTypeById(@PathVariable(name = "id") int id) {


        PostTypeDTO data = this.postTypeService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping({ "", "/" })
    public ResponseEntity<List<PostTypeDTO>> getAllPostTypes(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description
            ) {

        // Desglosar el parámetro 'sort' en campo y dirección
        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sortParams[0])));

        // Obtener los datos paginados y filtrados usando el servicio
        List<PostTypeDTO> postTypesPage = postTypeService.getAllPostTypes(pageable, name, description);

        // Retornar la respuesta con los datos paginados
        return ResponseEntity.ok(postTypesPage);
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<PostTypeDTO> create(@Valid @RequestBody() PostTypeDTO postTypeDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        PostTypeDTO data = this.postTypeService.create(postTypeDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @PutMapping({ "/{id}" })
    public ResponseEntity<PostTypeDTO> update(@Valid @PathVariable(name = "id", required = true) int id,
            @RequestBody() PostTypeDTO postTypeDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        postTypeDto.setId(id);
        PostTypeDTO data = this.postTypeService.update(postTypeDto);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @DeleteMapping({ "/{id}" })
    public ResponseEntity<String> delete( @PathVariable(name = "id", required = true) Integer id) {

        this.postTypeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("PostType with id: " + id + "was deleted");
    }

}
