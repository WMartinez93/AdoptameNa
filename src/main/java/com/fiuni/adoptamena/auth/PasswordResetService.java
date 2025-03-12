package com.fiuni.adoptamena.auth;

import com.fiuni.adoptamena.api.dao.user.IPasswordResetTokenDao;
import com.fiuni.adoptamena.api.dao.user.IUserDao;
import com.fiuni.adoptamena.api.domain.user.PasswordResetTokenDomain;
import com.fiuni.adoptamena.api.domain.user.UserDomain;
import com.fiuni.adoptamena.exception_handler.exceptions.BadRequestException;
import com.fiuni.adoptamena.exception_handler.exceptions.GoneException;
import com.fiuni.adoptamena.utils.EmailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final IPasswordResetTokenDao passwordResetTokenDao;
    private final IUserDao userDao;
    private final EmailService emailService;
    private final AuthService authService;

    private static final String BASE_RESET_PASSWORD_LINK = "https://test--relaxed-bienenstitch-1d8d39.netlify.app/reset-password?token=";

    /**
     * Crea un nuevo token de restablecimiento de contraseña para un usuario y lo
     * guarda en la base de datos.
     * 
     * @param user Usuario para el cual se genera el token
     * @return Token de restablecimiento de contraseña generado
     */
    @Transactional
    public String createResetToken(UserDomain user) {
        deleteToken(user);
        PasswordResetTokenDomain token = new PasswordResetTokenDomain(user);
        passwordResetTokenDao.save(token);
        return token.getToken();
    }

    /**
     * Obtiene un token de restablecimiento de contraseña desde la base de datos.
     * 
     * @param token Token de restablecimiento de contraseña
     * @return Instancia de PasswordResetTokenDomain correspondiente al token
     */
    public PasswordResetTokenDomain getResetToken(String token) {
        return passwordResetTokenDao.findByToken(token)
                .orElseThrow(
                        () -> new BadRequestException("Token de restablecimiento de contraseña inválido o expirado."));
    }

    /**
     * Elimina un token de restablecimiento de contraseña de la base de datos.
     * 
     * @param user Usuario asociado al token a eliminar
     */
    @Transactional
    public void deleteToken(UserDomain user) {
        PasswordResetTokenDomain token = passwordResetTokenDao.findByUser(user);
        if (token != null) {
            passwordResetTokenDao.delete(token);
        }
    }

    /**
     * Verifica el token de restablecimiento de contraseña.
     * 
     * @param token Token de restablecimiento de contraseña
     * @return Respuesta con mensaje de éxito
     */
    @Transactional
    public GenericResponse verifyResetToken(String token) {
        PasswordResetTokenDomain resetToken = getResetToken(token);

        if (resetToken.isExpired()) {
            deleteToken(resetToken.getUser());
            throw new GoneException("El token de restablecimiento de contraseña ha expirado. Solicita uno nuevo.");
        }

        return GenericResponse.builder()
                .message("Token de restablecimiento de contraseña válido.")
                .build();
    }

    /**
     * Permite al usuario establecer una nueva contraseña.
     * 
     * @param token       Token de restablecimiento de contraseña
     * @param newPassword Nueva contraseña del usuario
     * @return Respuesta con mensaje de éxito
     */
    @Transactional
    public GenericResponse resetPassword(String token, String newPassword) {
        PasswordResetTokenDomain resetToken = getResetToken(token);

        if (resetToken.isExpired()) {
            deleteToken(resetToken.getUser());
            throw new GoneException("El token de restablecimiento de contraseña ha expirado. Solicita uno nuevo.");
        }

        UserDomain user = resetToken.getUser();
        authService.setPassword(user.getEmail(), newPassword);

        deleteToken(user);

        return GenericResponse.builder()
                .message("Contraseña restablecida correctamente.")
                .build();
    }

    /**
     * Envía un correo de restablecimiento de contraseña a un usuario.
     * 
     * @param email Email del usuario
     * @return Respuesta con mensaje de éxito
     */
    public GenericResponse sendResetPasswordEmail(String email) {
        UserDomain user = userDao.findByEmailAndIsDeletedFalse(email)
                .orElseThrow(() -> new BadRequestException("Usuario no encontrado."));

        deleteToken(user);

        String token = createResetToken(user);
        emailService.sendResetPasswordEmail(user.getEmail(), BASE_RESET_PASSWORD_LINK + token);

        return GenericResponse.builder()
                .message("Correo de restablecimiento de contraseña enviado correctamente.")
                .build();
    }
}
