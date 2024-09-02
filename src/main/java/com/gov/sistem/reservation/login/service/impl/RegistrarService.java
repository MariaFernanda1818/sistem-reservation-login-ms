package com.gov.sistem.reservation.login.service.impl;

import com.gov.sistem.reservation.commons.dto.ClienteDTO;
import com.gov.sistem.reservation.commons.entity.ClienteEntity;
import com.gov.sistem.reservation.commons.util.enums.InicialesCodEnum;
import com.gov.sistem.reservation.commons.util.helper.Utilidades;
import com.gov.sistem.reservation.commons.util.mapper.ClienteMapper;
import com.gov.sistem.reservation.login.dto.ResponseTokenDTO;
import com.gov.sistem.reservation.login.dto.RespuestaGeneralDTO;
import com.gov.sistem.reservation.login.jpa.repository.ClienteRepository;
import com.gov.sistem.reservation.login.service.IRegistrarService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Servicio para manejar el registro de nuevos clientes.
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class RegistrarService implements IRegistrarService {
    private final ClienteRepository clienteRepository;

    private final ClienteMapper clienteMapper;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    /**
     * Registra un nuevo cliente y devuelve un token JWT.
     *
     * @param cliente Datos del cliente a registrar.
     * @return Respuesta que contiene el token JWT o un mensaje de error.
     */
    @Override
    @Transactional
    public RespuestaGeneralDTO registrar(ClienteDTO cliente) {
        RespuestaGeneralDTO respuestaGeneralDTO = new RespuestaGeneralDTO();

        try {
            // Codifica la contraseña antes de guardar el cliente
            cliente.setContrasenaCliente(passwordEncoder.encode(cliente.getContrasenaCliente()));

            cliente.setCodigoCliente(Utilidades.generarCodigo(InicialesCodEnum.CLIE));
            // Guarda el cliente en la base de datos
            ClienteEntity clienteEntity = clienteMapper.dtoToEntity(cliente);

            // Mapea el cliente guardado a DTO
            ClienteDTO savedCliente = clienteMapper.entityToDto(clienteRepository.save(clienteEntity));


            // Crea un objeto User para el JWT
            UserDetails user = new org.springframework.security.core.userdetails.User(
                    savedCliente.getCorreoCliente(),
                    savedCliente.getContrasenaCliente(),
                    Collections.emptyList() // Aquí podrías agregar roles si los tuvieses
            );

            // Genera el token JWT
            String token = jwtService.getToken(user);
            ResponseTokenDTO response = new ResponseTokenDTO();
            response.setToken(token);
            respuestaGeneralDTO.setData(response);
            respuestaGeneralDTO.setStatus(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al registrar el cliente: {}", e.getMessage(), e);
            respuestaGeneralDTO.setData(null);
            respuestaGeneralDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            respuestaGeneralDTO.setMensaje("Error al registrar el cliente");
        }

        return respuestaGeneralDTO;
    }

}
