package com.gov.sistem.reservation.login.util.configuration;

import com.gov.sistem.reservation.login.jpa.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

/**
 * Configuración de seguridad de la aplicación.
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {


    private final ClienteRepository clienteRepository;

    /**
     * Configura el AuthenticationManager.
     *
     * @param config La configuración de autenticación.
     * @return El AuthenticationManager configurado.
     * @throws Exception Si ocurre un error al obtener el AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configura el AuthenticationProvider.
     *
     * @return El AuthenticationProvider configurado.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        /*
        * Crea una instancia del DaoAuthenticationProvider, que es responsable de autenticar a los usuarios contra una base de datos.
        * */
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Configura el PasswordEncoder.
     *
     * @return El PasswordEncoder configurado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        /*
        * Retorna una instancia de BCryptPasswordEncoder, que es un tipo de PasswordEncoder que utiliza el algoritmo BCrypt para encriptar contraseñas.
        * */
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el UserDetailsService.
     *
     * @return El UserDetailsService configurado.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> clienteRepository.findAllByCorreoCliente(username)
                .stream()
                .map(cliente -> new org.springframework.security.core.userdetails.User(
                        cliente.getCorreoCliente(),
                        cliente.getContrasenaCliente(),
                        Collections.emptyList() // Aquí podrías agregar roles si los tuvieses
                ))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
