package com.fiuni.adoptamena.api.controller.post;

import com.fiuni.adoptamena.api.dto.post.ReportReasonsDTO;
import com.fiuni.adoptamena.api.service.post.IReportReasonsService;
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
@RequestMapping("/reportReasons")
@Tag(name = "Report Reason")
public class ReportReasonsController {

    @Autowired
    private IReportReasonsService reportReasonsService;

    @PostMapping({ "", "/" })
    public ResponseEntity<ReportReasonsDTO> create(@Valid @RequestBody() ReportReasonsDTO reportReasonsDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        ReportReasonsDTO data = this.reportReasonsService.create(reportReasonsDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @PutMapping({ "/{id}" })
    public ResponseEntity<ReportReasonsDTO> update(@Valid @PathVariable(name = "id", required = true) int id,
            @RequestBody() ReportReasonsDTO reportReasonsDto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors());
        }

        reportReasonsDto.setId(id);
        ReportReasonsDTO data = this.reportReasonsService.update(reportReasonsDto);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @DeleteMapping({ "/{id}" })
    public ResponseEntity<String> delete(@Valid @PathVariable(name = "id", required = true) Integer id) {

        this.reportReasonsService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Report Reason with id: " + id + "was deleted");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportReasonsDTO> getReportReasonById(@PathVariable(name = "id", required = true) int id) {

        ReportReasonsDTO data = this.reportReasonsService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @GetMapping({ "", "/" })
    public ResponseEntity<List<ReportReasonsDTO>> getAllReportReasons(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String sort,
            @RequestParam(value = "description", required = false) String description) {

        String[] sortParams = sort.split(",");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc(sortParams[0])));

        List<ReportReasonsDTO> reportReasonsPage = reportReasonsService.getAllReportReasons(pageable, description);

        return ResponseEntity.status(HttpStatus.OK).body(reportReasonsPage);
    }

}
