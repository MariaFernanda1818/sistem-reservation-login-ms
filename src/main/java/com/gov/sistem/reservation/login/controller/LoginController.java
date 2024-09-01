package com.gov.sistem.reservation.login.controller;

import com.gov.sistem.reservation.commons.dto.ClienteDTO;
import com.gov.sistem.reservation.login.dto.RespuestaGeneralDTO;
import com.gov.sistem.reservation.login.service.ILoginService;
import com.gov.sistem.reservation.login.service.IRegistrarService;
import com.gov.sistem.reservation.login.util.helper.ApiEndpointsContants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;


/**
 * Controlador para manejar las solicitudes de autenticación y registro de usuarios.
 *
 * Este controlador expone dos endpoints para iniciar sesión y registrar usuarios.
 * Utiliza los servicios ILoginService e IRegistrarService para realizar las operaciones.
 */
@RestController
@RequestMapping(ApiEndpointsContants.BASE_LOGIN)
@RequiredArgsConstructor
public class LoginController {

    private final ILoginService iLoginService;

    private final IRegistrarService iRegistrarService;

    /**
     * Maneja la solicitud de inicio de sesión.
     *
     * @param cliente Los datos del cliente para iniciar sesión.
     * @return Una respuesta que contiene el token JWT y el estado de la operación.
     * @throws NoSuchAlgorithmException Si ocurre un error al procesar la contraseña.
     */
    @PostMapping(ApiEndpointsContants.LOGIN_INICIO)
    public ResponseEntity<RespuestaGeneralDTO> login(@RequestBody ClienteDTO cliente) throws NoSuchAlgorithmException {
        RespuestaGeneralDTO respuestaGeneralDTO = iLoginService.login(cliente);
        return new ResponseEntity<>(respuestaGeneralDTO, respuestaGeneralDTO.getStatus());
    }

    /**
     * Maneja la solicitud de registro de un nuevo usuario.
     *
     * @param cliente Los datos del cliente para registrar.
     * @return Una respuesta que contiene el resultado del registro y el estado de la operación.
     */
    @PostMapping(ApiEndpointsContants.LOGIN_REGISTRAR)
    public ResponseEntity<RespuestaGeneralDTO> registrar(@RequestBody ClienteDTO cliente) {
        RespuestaGeneralDTO respuestaGeneralDTO = iRegistrarService.registrar(cliente);
        return new ResponseEntity<>(respuestaGeneralDTO, respuestaGeneralDTO.getStatus());
    }

}
