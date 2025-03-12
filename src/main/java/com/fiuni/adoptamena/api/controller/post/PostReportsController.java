package com.fiuni.adoptamena.api.controller.post;


import com.fiuni.adoptamena.api.dto.post.PostReportDto;
import com.fiuni.adoptamena.api.service.post.IpostReportService;
import com.fiuni.adoptamena.exception_handler.exceptions.BadRequestException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/postReports")
@Tag(name = "Post Report")
public class PostReportsController {

    @Autowired
    private IpostReportService postReportService;

    @PostMapping({ "", "/" })
    public ResponseEntity<PostReportDto> create(@Valid @RequestBody() PostReportDto postReportDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        PostReportDto data = this.postReportService.create(postReportDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @PutMapping({ "/{id}" })
    public ResponseEntity<PostReportDto> update(@Valid @PathVariable(name = "id", required = true) Integer id,
                                                @RequestBody PostReportDto postReportDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        postReportDto.setId(id);
        PostReportDto data = this.postReportService.update(postReportDto);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @DeleteMapping({ "/{id}" })
    public ResponseEntity<String> delete(@Valid @PathVariable(name = "id", required = true) Integer id) {

        this.postReportService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Post Report with id: " + id + "was deleted");
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostReportDto> getPostReportById(@PathVariable(name = "id", required = true) Integer id) {

        PostReportDto data = this.postReportService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping({ "", "/" })
    public ResponseEntity<List<PostReportDto>> getAllPostReports(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort,
            @RequestParam(value = "user", required = false) Integer userId, //filtro por usuario
            @RequestParam(value = "post", required = false) Integer postId, //filtro por Post
            @RequestParam(value = "reportReason", required = false) Integer reportReasonsId, //filtro por razones de reportes
            @RequestParam(value = "description", required = false) String description //filtro por descripcion
    ) {

        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sortParams[0])));

        List<PostReportDto> postReportsPage = postReportService.getAllPostsReports(pageable, userId, postId, reportReasonsId, description);
        return ResponseEntity.ok(postReportsPage);
    }

}
