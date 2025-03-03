package com.fiuni.adoptamena.auth;

import com.fiuni.adoptamena.api.dao.user.IRoleDao;
import com.fiuni.adoptamena.api.dao.user.IUserDao;
import com.fiuni.adoptamena.api.domain.user.UserDomain;
import com.fiuni.adoptamena.api.dto.profile.ProfileDTO;
import com.fiuni.adoptamena.api.service.profile.IProfileService;
import com.fiuni.adoptamena.exception_handler.exceptions.BadRequestException;
import com.fiuni.adoptamena.exception_handler.exceptions.ForbiddenException;
import com.fiuni.adoptamena.jwt.JwtService;

import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Autowired
    private IUserDao userDao;

    @Autowired
    private IRoleDao roleDao;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private IProfileService profileService;

    @Autowired
    private VerificationTokenService verificationTokenService;

    private static final Set<String> VALID_ROLES = Set.of("USER", "ORGANIZATION");

    public AuthResponse login(LoginRequest request) {
        // Autenticar al usuario con email y contraseña
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (Exception e) {
            throw new BadCredentialsException("Email o contraseña incorrectos.");
        }

        // Buscar el usuario en la base de datos
        UserDomain user = userDao.findByEmailAndIsDeletedFalse(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con el email: " + request.getEmail()));

        // Verificar si la cuenta está verificada
        if (!user.getIsVerified()) {
            log.info("Cuenta no verificada: {}", user.getEmail());
            throw new ForbiddenException("La cuenta no está verificada. Revisa tu email para verificar tu cuenta.");
        }

        // Generar y devolver el token de autenticación
        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }

    @Transactional
    public GenericResponse register(RegisterRequest request, boolean sendEmail) {
        // Validar el rol antes de continuar
        String roleName = request.getRole().toUpperCase();
        if (!VALID_ROLES.contains(roleName)) {
            throw new BadRequestException("Rol inválido. Debe ser 'USER' o 'ORGANIZATION'");
        }

        try {
            // Crear usuario
            UserDomain user = new UserDomain();
            user.setUsername(request.getEmail());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(
                    roleDao.findByName(roleName.toLowerCase())
                            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName)));
            user.setIsDeleted(false);
            user.setIsVerified(false);
            user.setCreationDate(new Date());

            // Guardar usuario en la base de datos
            UserDomain createdUser = userDao.save(user);

            // Crear perfil vacío asociado al usuario
            ProfileDTO profile = new ProfileDTO();
            profile.setId(createdUser.getId());
            profile.setFullName(request.getFullName());

            // Guardar nombre de la organización si es necesario
            if (roleName.equals("ORGANIZATION")) {
                if (request.getOrganizationName() == null) {
                    throw new BadRequestException(
                            "El nombre de la organización es requerido para el rol 'ORGANIZATION'");
                }
                profile.setOrganizationName(request.getOrganizationName());

            }
            profileService.create(profile);

            // Enviar email de verificación si es necesario
            if (sendEmail) {
                verificationTokenService.sendVerificationEmail(user.getEmail());
            }

            return GenericResponse.builder()
                    .message("Usuario registrado exitosamente. Revisa tu email para verificar tu cuenta.")
                    .build();

        } catch (DataIntegrityViolationException e) {
            Throwable cause = e.getMostSpecificCause();

            if (cause instanceof SQLIntegrityConstraintViolationException) {
                // Si el error es por restricción UNIQUE
                if (cause.getMessage().contains("usuarios_email_key")) {
                    log.error("Intento de registro con email duplicado: {}", request.getEmail());
                    throw new BadRequestException("El email ya está registrado");
                }
            }

            log.error("Error de integridad en la base de datos", e);
            throw new BadRequestException("Violación de integridad de datos");

        } catch (ConstraintViolationException e) {
            log.error("Error de validación de datos: {}", e.getConstraintViolations());
            throw new BadRequestException("Datos inválidos: " + e.getMessage());

        } catch (PersistenceException e) {
            log.error("Error de persistencia en la base de datos", e);
            throw new RuntimeException("Error de persistencia en la base de datos.");

        } catch (Exception e) {
            log.error("Error inesperado al registrar usuario", e);
            throw new RuntimeException("Error inesperado al registrar usuario.");
        }
    }

}