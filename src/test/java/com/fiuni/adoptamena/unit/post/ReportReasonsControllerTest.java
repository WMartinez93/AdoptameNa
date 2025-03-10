package com.fiuni.adoptamena.unit.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiuni.adoptamena.api.controller.post.ReportReasonsController;
import com.fiuni.adoptamena.api.dto.post.ReportReasonsDTO;
import com.fiuni.adoptamena.api.service.post.IReportReasonsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ReportReasonsController.class)
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"ADMIN"}) // Se agrega rol ADMIN para evitar 403
public class ReportReasonsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IReportReasonsService reportReasonsService;

    @MockBean
    private com.fiuni.adoptamena.jwt.JwtService jwtService;

    private ReportReasonsDTO reportReason;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        reportReason = new ReportReasonsDTO("Contenido ofensivo");
    }

    @Test
    void testCreateReportReason() throws Exception {
        when(reportReasonsService.create(any(ReportReasonsDTO.class))).thenReturn(reportReason);

        mockMvc.perform(MockMvcRequestBuilders.post("/reportReasons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportReason))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Agregar token CSRF
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reportReason.getId()))
                .andExpect(jsonPath("$.description").value(reportReason.getDescription()));
    }

    @Test
    void testGetReportReasonById() throws Exception {
        when(reportReasonsService.getById(1)).thenReturn(reportReason);

        mockMvc.perform(MockMvcRequestBuilders.get("/reportReasons/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reportReason.getId()))
                .andExpect(jsonPath("$.description").value(reportReason.getDescription()));
    }

    @Test
    void testUpdateReportReason() throws Exception {
        when(reportReasonsService.update(any(ReportReasonsDTO.class))).thenReturn(reportReason);

        mockMvc.perform(MockMvcRequestBuilders.put("/reportReasons/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()) // Agregar token CSRF
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reportReason)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reportReason.getId()))
                .andExpect(jsonPath("$.description").value(reportReason.getDescription()));
    }

    @Test
    void testDeleteReportReason() throws Exception {
        doNothing().when(reportReasonsService).delete(1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/reportReasons/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Agregar token CSRF
                .andExpect(status().isNoContent());
    }
}
