package com.gov.sistem.reservation.login.service.impl;

import com.gov.sistem.reservation.commons.dto.ClienteDTO;
import com.gov.sistem.reservation.commons.util.mapper.ClienteMapper;
import com.gov.sistem.reservation.login.dto.ResponseTokenDTO;
import com.gov.sistem.reservation.login.dto.RespuestaGeneralDTO;
import com.gov.sistem.reservation.login.jpa.repository.ClienteRepository;
import com.gov.sistem.reservation.login.service.ILoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Servicio para manejar el inicio de sesión de usuarios.
 */
@Service
@RequiredArgsConstructor
public class LoginService implements ILoginService {

    private final ClienteRepository clienteRepository;

    private final JwtService jwtService;

    private final ClienteMapper clienteMapper;

    private final AuthenticationManager authenticationManager;


    /**
     * Maneja el inicio de sesión del cliente y devuelve un JWT.
     *
     * @param cliente Datos del cliente para iniciar sesión.
     * @return Respuesta que contiene el token JWT o un mensaje de error.
     */
    @Override
    public RespuestaGeneralDTO login(ClienteDTO cliente) {
        RespuestaGeneralDTO respuestaGeneralDTO = new RespuestaGeneralDTO();

        try {
            // Autenticación del cliente
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(cliente.getCorreoCliente(), cliente.getContrasenaCliente())
            );

            // Obtiene los detalles del cliente
            ClienteDTO clienteDTO = clienteMapper.entityToDto(clienteRepository.findByCorreoCliente(cliente.getCorreoCliente()));

            if(clienteDTO == null){
                return respuestaGeneralDTO;
            }
            // Crea un objeto User para el JWT
            UserDetails user = new org.springframework.security.core.userdetails.User(
                    clienteDTO.getCorreoCliente(),
                    clienteDTO.getContrasenaCliente(),
                    Collections.emptyList() // Aquí podrías agregar roles si los tuvieses
            );
            // Genera el token JWT
            String token = jwtService.getToken(user);
            ResponseTokenDTO response = new ResponseTokenDTO();
            response.setToken(token);
            response.setCliente(clienteDTO);
            respuestaGeneralDTO.setData(response);
            respuestaGeneralDTO.setMensaje("Sesión iniciada!");
            respuestaGeneralDTO.setStatus(HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            respuestaGeneralDTO.setData(null);
            respuestaGeneralDTO.setStatus(HttpStatus.UNAUTHORIZED);
            respuestaGeneralDTO.setMensaje("Invalid username or password");
        } catch (BadCredentialsException e) {
            respuestaGeneralDTO.setData(null);
            respuestaGeneralDTO.setStatus(HttpStatus.UNAUTHORIZED);
            respuestaGeneralDTO.setMensaje("Invalid credentials provided");
        } catch (Exception e) {
            respuestaGeneralDTO.setData(null);
            respuestaGeneralDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            respuestaGeneralDTO.setMensaje("An error occurred during login");
        }

        return respuestaGeneralDTO;
    }
}
