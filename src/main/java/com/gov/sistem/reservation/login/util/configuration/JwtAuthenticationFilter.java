package com.gov.sistem.reservation.login.util.configuration;

import com.gov.sistem.reservation.login.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//OncePerRequestFilter -> Sirve para crear filtros personalizados
//Esto sirve o funciona cuando se ejecutan peticiones http
/**
 * Filtro de autenticación JWT que se ejecuta en cada solicitud HTTP.
 * Extiende {@link OncePerRequestFilter} para asegurar que el filtro se ejecute una sola vez por solicitud.
 */
@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService; // Servicio para manejar la lógica JWT
    private final UserDetailsService userDetailsService; // Servicio para cargar detalles del usuario

    /**
     * Realiza el filtrado de la solicitud HTTP para verificar la validez del token JWT.
     *
     * @param request La solicitud HTTP que se está filtrando.
     * @param response La respuesta HTTP que se está generando.
     * @param filterChain La cadena de filtros para la solicitud.
     * @throws ServletException Si ocurre un error al procesar la solicitud.
     * @throws IOException Si ocurre un error al leer la solicitud o escribir la respuesta.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String token = getTokenFromRequest(request); // Obtiene el token del encabezado de la solicitud

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String username = jwtService.getUsernameFromToken(token); // Extrae el nombre de usuario del token

            if (username != null && jwtService.isTokenValid(token, userDetailsService.loadUserByUsername(username))) {
                // Crea el objeto de autenticación y establece los detalles de la solicitud
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Establece la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continúa con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }

    /**
     * Extrae el token JWT del encabezado Authorization de la solicitud HTTP.
     *
     * @param request La solicitud HTTP de la cual se extrae el token.
     * @return El token JWT extraído, o null si no se encuentra.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // Obtiene el encabezado de autorización

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // Extrae el token del encabezado
        }
        return null; // Retorna null si no se encuentra el token
    }
}