package com.fiuni.adoptamena.unit.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiuni.adoptamena.auth.LoginRequest;
import com.fiuni.adoptamena.auth.RegisterRequest;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        //@Autowired
        //private IUserDao IUserDao;
        /*
         * @Test
         * void testRegisterAndLoginFlowWithVerifiedAcc() throws Exception {
         * String fullName = "User Test";
         * String email = "usertest@example.com";
         * String password = "password123";
         * String role = "USER";
         * RegisterRequest registerRequest = new RegisterRequest(null, fullName, email,
         * password, role);
         * 
         * mockMvc.perform(post("/auth/register")
         * .contentType(MediaType.APPLICATION_JSON)
         * .content(objectMapper.writeValueAsString(registerRequest)))
         * .andExpect(status().isOk())
         * .andExpect(jsonPath("$.message").exists());
         * 
         * UserDomain createdUser = IUserDao.findByEmail(email).orElseThrow();
         * createdUser.setIsVerified(true);
         * IUserDao.save(createdUser);
         * 
         * LoginRequest loginRequest = new LoginRequest(email, password);
         * mockMvc.perform(post("/auth/login")
         * .contentType(MediaType.APPLICATION_JSON)
         * .content(objectMapper.writeValueAsString(loginRequest)))
         * .andExpect(status().isOk())
         * .andExpect(jsonPath("$.token").exists());
         * }
         */

        @Test
        void testRegisterAndLoginFlowWithoutVerifiedAcc() throws Exception {
                String fullName = "User Test";
                String email = "usertest@example.com";
                String password = "password123";
                String role = "USER";
                RegisterRequest registerRequest = new RegisterRequest(null, fullName, email, password, role);

                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerRequest)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.message").exists());

                LoginRequest loginRequest = new LoginRequest(email, password);
                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isForbidden())
                                .andExpect(jsonPath("$.message").exists());
        }

        @Test
        void testRegisterWithInvalidRole() throws Exception {
                RegisterRequest request = new RegisterRequest(null, "Valid Username", "valid@example.com",
                                "password123",
                                "Invalid");

                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testRegisterWithExistingEmail() throws Exception {
                String fullName = "User Test";
                String email = "existing@example.com";
                String password = "password123";
                String role = "USER";
                RegisterRequest request = new RegisterRequest(null, fullName, email, password, role);

                // Registrar un usuario
                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isOk());

                // Intentar registrar un usuario con el mismo email
                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isConflict());
        }

        @Test
        void testLoginWithInvalidCredentials() throws Exception {
                LoginRequest loginRequest = new LoginRequest("nonexistent@example.com", "wrongpassword");

                // Intentar iniciar sesión con credenciales inválidas
                mockMvc.perform(post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        void testRegisterWithInvalidEmail() throws Exception {
                RegisterRequest request = new RegisterRequest(null, "Valid Username", "invalidemail", "password123",
                                "USER");

                // Intentar registrar un usuario con email inválido
                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testRegisterWithShortPassword() throws Exception {
                RegisterRequest request = new RegisterRequest(null, "Valid Username", "valid@example.com", "123",
                                "USER");

                mockMvc.perform(post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest());
        }
}
