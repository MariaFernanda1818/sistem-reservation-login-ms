package com.gov.sistem.reservation.login.util.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad para la aplicación.
 *
 * Esta clase configura la seguridad HTTP para la aplicación, incluyendo la gestión de autenticación y autorización.
 * Desactiva la protección CSRF, configura las reglas de autorización de solicitudes, y establece la política de sesión.
 * También configura un filtro de autenticación JWT personalizado y un proveedor de autenticación.
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Filtro personalizado para la autenticación JWT
    private final AuthenticationProvider authenticationProvider; // Proveedor de autenticación

    /**
     * Configura la cadena de filtros de seguridad para la aplicación.
     *
     * @param http La configuración de seguridad HTTP.
     * @return La cadena de filtros de seguridad configurada.
     * @throws Exception Si ocurre un error al configurar la seguridad.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desactiva la protección CSRF, ya que se está usando autenticación basada en token
                .csrf(csrf -> csrf.disable())
                // Configura las reglas de autorización de las solicitudes
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/**").permitAll() // Permite acceso a la URL /login/** sin autenticación
                )
                // Configura el manejo de sesiones
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Establece la política de sesión como sin estado
                // Configura el proveedor de autenticación
                .authenticationProvider(authenticationProvider)
                // Agrega el filtro JWT antes del filtro de autenticación de usuario y contraseña
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build(); // Construye la configuración de seguridad
    }
}