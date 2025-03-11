package com.fiuni.adoptamena.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fiuni.adoptamena.auth.CustomUserDetails;
import com.fiuni.adoptamena.exception_handler.ErrorResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.ExpiredJwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    @SuppressWarnings("null")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Excluir rutas públicas para que no pasen por el filtro
        if (requestURI.startsWith("/auth/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = getTokenFromRequest(request);
        final String username;

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Obtener el nombre de usuario desde el token
            username = jwtService.getUsernameFromToken(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
                // Verificar si el token es válido
                if (jwtService.isTokenValid(token, userDetails)) {
                    Object rolesClaim = jwtService.getClaim(token, claims -> claims.get("roles"));
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

                    if (rolesClaim instanceof String rolesString && !rolesString.isEmpty()) {
                        authorities = Arrays.stream(rolesString.split(","))
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());
                    }

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            authorities);

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            sendErrorResponse(response, "Token expirado", ex.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (JwtException ex) {
            sendErrorResponse(response, "Token inválido", ex.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception ex) {
            sendErrorResponse(response, "Error en la autenticación", ex.getMessage(),
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, String error, String message, int status)
            throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(error, message);
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(errorResponse.toString());
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}