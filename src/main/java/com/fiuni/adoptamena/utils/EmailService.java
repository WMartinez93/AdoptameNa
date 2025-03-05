package com.fiuni.adoptamena.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendVerificationEmail(String to, String verificationLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Confirma tu cuenta en Adoptamena");

            String imageUrl = "https://i.imgur.com/hyzAvP8.png";

            // HTML con imagen + mensaje mejorado
            String htmlContent = "<div style='font-family: Arial, sans-serif; text-align: center; padding: 20px; background-color: #f4f4f4;'>"
                    + "<div style='max-width: 500px; background: white; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); margin: auto;'>"
                    + "<img src='" + imageUrl + "' alt='Adoptamena' style='width: 300px; margin-top: 20px;'>"
                    + "<h2 style='color: #333;'>¡Gracias por unirte a Adoptamena!</h2>"
                    + "<p style='font-size: 16px; color: #555;'>Gracias por unirte a la comunidad de Adoptamena y ser parte del cambio.</p>"
                    + "<p style='font-size: 16px; color: #555;'>Para empezar, da clic en el botón de abajo:</p>"
                    + "<a href='" + verificationLink
                    + "' style='display: inline-block; padding: 12px 24px; margin-top: 10px; font-size: 16px; "
                    + "color: white; background-color: #007bff; text-decoration: none; border-radius: 5px;'>Verificar cuenta</a>"
                    + "<p style='margin-top: 20px; font-size: 14px; color: #777;'>Si no solicitaste este correo, ignóralo.</p>"
                    + "</div></div>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error al enviar el correo de verificación", e);
            throw new RuntimeException("No se pudo enviar el correo de verificación.");
        }
    }

    // Método para enviar el correo de restablecimiento de contraseña
    @Async
    public void sendResetPasswordEmail(String to, String resetPasswordLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Restablecimiento de contraseña en Adoptamena");

            String imageUrl = "https://i.imgur.com/hyzAvP8.png";

            // HTML con imagen + mensaje mejorado
            String htmlContent = "<div style='font-family: Arial, sans-serif; text-align: center; padding: 20px; background-color: #f4f4f4;'>"
                    + "<div style='max-width: 500px; background: white; padding: 20px; border-radius: 8px; box-shadow: 0px 0px 10px rgba(0,0,0,0.1); margin: auto;'>"
                    + "<img src='" + imageUrl + "' alt='Adoptamena' style='width: 300px; margin-top: 20px;'>"
                    + "<h2 style='color: #333;'>Restablece tu contraseña en Adoptamena</h2>"
                    + "<p style='font-size: 16px; color: #555;'>Recibiste este correo porque solicitaste restablecer tu contraseña en Adoptamena.</p>"
                    + "<p style='font-size: 16px; color: #555;'>Para restablecer tu contraseña, haz clic en el siguiente enlace:</p>"
                    + "<a href='" + resetPasswordLink
                    + "' style='display: inline-block; padding: 12px 24px; margin-top: 10px; font-size: 16px; "
                    + "color: white; background-color: #007bff; text-decoration: none; border-radius: 5px;'>Restablecer contraseña</a>"
                    + "<p style='margin-top: 20px; font-size: 14px; color: #777;'>Si no solicitaste este correo, ignóralo.</p>"
                    + "</div></div>";

            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            log.error("Error al enviar el correo de restablecimiento de contraseña", e);
            throw new RuntimeException("No se pudo enviar el correo de restablecimiento de contraseña.");
        }
    }
}
