package com.fiuni.adoptamena.unit.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiuni.adoptamena.api.controller.post.PostReportsController;
import com.fiuni.adoptamena.api.dto.post.PostReportDto;
import com.fiuni.adoptamena.api.service.post.IpostReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PostReportsController.class)
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", roles = {"ADMIN"}) // Se agrega rol ADMIN para evitar 403
public class PostReportsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IpostReportService postReportService;

    @MockBean
    private com.fiuni.adoptamena.jwt.JwtService jwtService;

    private PostReportDto postReportDto;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        postReportDto = new PostReportDto(1, 2, 3, "Descripción del reporte", new java.util.Date(), "activo");
    }

    @Test
    void testCreatePostReport() throws Exception {
        // Simular que el servicio devuelve un PostReportDto cuando se crea un reporte
        when(postReportService.create(Mockito.any(PostReportDto.class))).thenReturn(postReportDto);

        // Realizar la solicitud POST
        mockMvc.perform(MockMvcRequestBuilders.post("/api/post-reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postReportDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Agregar token CSRF
                .andExpect(status().isCreated()) // Esperamos un 201 Created
                .andExpect(jsonPath("$.idUser").value(postReportDto.getIdUser()))
                .andExpect(jsonPath("$.idPost").value(postReportDto.getIdPost()))
                .andExpect(jsonPath("$.idReportReason").value(postReportDto.getIdReportReason()))
                .andExpect(jsonPath("$.description").value(postReportDto.getDescription()))
                .andExpect(jsonPath("$.status").value(postReportDto.getStatus()));

    }

    @Test
    void testCreatePostReport_withNullFields() throws Exception {
        // Crear un PostReportDto con campos nulos para probar la validación
        PostReportDto invalidPostReportDto = new PostReportDto(null, null, null, null, null, null);

        // Realizar la solicitud POST
        mockMvc.perform(MockMvcRequestBuilders.post("/api/post-reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPostReportDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Agregar token CSRF
                .andExpect(status().isBadRequest()) // Esperamos un 400 Bad Request
                .andExpect(jsonPath("$.errors[0]").value("El usuario debe ser Obligatorio."))
                .andExpect(jsonPath("$.errors[1]").value("El post debe ser Obligatorio."))
                .andExpect(jsonPath("$.errors[2]").value("La razon del reporte debe ser Obligatorio."));

    }

    @Test
    void testGetPostReportById() throws Exception {
        // Simular que el servicio devuelve un PostReportDto cuando se obtiene el reporte por ID
        when(postReportService.getById(1)).thenReturn(postReportDto);

        // Realizar la solicitud GET
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post-reports/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Agregar token CSRF
                .andExpect(status().isOk()) // Esperamos un 200 OK
                .andExpect(jsonPath("$.idUser").value(postReportDto.getIdUser()))
                .andExpect(jsonPath("$.idPost").value(postReportDto.getIdPost()))
                .andExpect(jsonPath("$.idReportReason").value(postReportDto.getIdReportReason()))
                .andExpect(jsonPath("$.description").value(postReportDto.getDescription()))
                .andExpect(jsonPath("$.status").value(postReportDto.getStatus()));

    }

    @Test
    void testGetPostReportById_NotFound() throws Exception {
        // Simular que no se encuentra el reporte
        when(postReportService.getById(999)).thenReturn(null);

        // Realizar la solicitud GET para un ID inexistente
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post-reports/999")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Agregar token CSRF
                .andExpect(status().isNotFound()); // Esperamos un 404 Not Found

    }

    @Test
    void testUpdatePostReport() throws Exception {
        PostReportDto updatedPostReportDto = new PostReportDto(1, 2, 3, "Descripción actualizada", new java.util.Date(), "activo");

        // Simular que el servicio devuelve el reporte actualizado
        when(postReportService.update(Mockito.any(PostReportDto.class))).thenReturn(updatedPostReportDto);

        // Realizar la solicitud PUT
        mockMvc.perform(MockMvcRequestBuilders.put("/api/post-reports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPostReportDto))
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Agregar token CSRF
                .andExpect(status().isOk()) // Esperamos un 200 OK
                .andExpect(jsonPath("$.idUser").value(updatedPostReportDto.getIdUser()))
                .andExpect(jsonPath("$.idPost").value(updatedPostReportDto.getIdPost()))
                .andExpect(jsonPath("$.idReportReason").value(updatedPostReportDto.getIdReportReason()))
                .andExpect(jsonPath("$.description").value(updatedPostReportDto.getDescription()))
                .andExpect(jsonPath("$.status").value(updatedPostReportDto.getStatus()));
    }

    @Test
    void testDeletePostReport() throws Exception {
        // Realizar la solicitud DELETE
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/post-reports/1")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Agregar token CSRF
                .andExpect(status().isNoContent()); // Esperamos un 204 No Content
    }

    @Test
    void testDeletePostReport_NotFound() throws Exception {
        // Simular que el reporte no existe
        when(postReportService.getById(999)).thenReturn(null);

        // Realizar la solicitud DELETE para un ID inexistente
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/post-reports/999")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Agregar token CSRF
                .andExpect(status().isNotFound()); // Esperamos un 404 Not Found
    }

    @Test
    void testGetAllPostReports_withFilters() throws Exception {
        // Datos de prueba para los filtros
        Integer userId = 1;
        Integer postId = 2;
        Integer reportReasonsId = 3;
        String description = "Descripción del reporte";

        // Crear una lista de reportes simulada
        List<PostReportDto> postReportDtos = List.of(
                new PostReportDto(1, 2, 3, "Descripción del reporte", new java.util.Date(), "activo"),
                new PostReportDto(1, 2, 3, "Otra descripción", new java.util.Date(), "activo")
        );

        // Simular que el servicio devuelve los reportes filtrados
        when(postReportService.getAllPostsReports(Mockito.any(Pageable.class), Mockito.eq(userId), Mockito.eq(postId), Mockito.eq(reportReasonsId), Mockito.eq(description)))
                .thenReturn(postReportDtos);

        // Realizar la solicitud GET con filtros
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post-reports")
                        .param("userId", "1")
                        .param("postId", "2")
                        .param("reportReasonsId", "3")
                        .param("description", "Descripción del reporte")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Agregar token CSRF
                .andExpect(status().isOk()) // Esperamos un 200 OK
                .andExpect(jsonPath("$.length()").value(postReportDtos.size())) // Verificar el número de elementos
                .andExpect(jsonPath("$[0].idUser").value(postReportDtos.get(0).getIdUser()))
                .andExpect(jsonPath("$[0].idPost").value(postReportDtos.get(0).getIdPost()))
                .andExpect(jsonPath("$[0].idReportReason").value(postReportDtos.get(0).getIdReportReason()))
                .andExpect(jsonPath("$[0].description").value(postReportDtos.get(0).getDescription()))
                .andExpect(jsonPath("$[0].status").value(postReportDtos.get(0).getStatus()));
    }

    @Test
    void testGetAllPostReports_withoutFilters() throws Exception {
        // Crear una lista de reportes simulada
        List<PostReportDto> postReportDtos = List.of(
                new PostReportDto(1, 2, 3, "Descripción del reporte", new java.util.Date(), "activo"),
                new PostReportDto(1, 2, 3, "Otra descripción", new java.util.Date(), "activo")
        );

        // Simular que el servicio devuelve todos los reportes sin filtros
        when(postReportService.getAllPostsReports(Mockito.any(Pageable.class), Mockito.isNull(), Mockito.isNull(), Mockito.isNull(), Mockito.isNull()))
                .thenReturn(postReportDtos);

        // Realizar la solicitud GET sin filtros
        mockMvc.perform(MockMvcRequestBuilders.get("/api/post-reports")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) // Agregar token CSRF
                .andExpect(status().isOk()) // Esperamos un 200 OK
                .andExpect(jsonPath("$.length()").value(postReportDtos.size())) // Verificar el número de elementos
                .andExpect(jsonPath("$[0].idUser").value(postReportDtos.get(0).getIdUser()))
                .andExpect(jsonPath("$[0].idPost").value(postReportDtos.get(0).getIdPost()))
                .andExpect(jsonPath("$[0].idReportReason").value(postReportDtos.get(0).getIdReportReason()))
                .andExpect(jsonPath("$[0].description").value(postReportDtos.get(0).getDescription()))
                .andExpect(jsonPath("$[0].status").value(postReportDtos.get(0).getStatus()));
    }

}
