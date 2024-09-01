package com.gov.sistem.reservation.login.service;

import com.gov.sistem.reservation.commons.dto.ClienteDTO;
import com.gov.sistem.reservation.login.dto.RespuestaGeneralDTO;

public interface IRegistrarService {

    RespuestaGeneralDTO registrar(ClienteDTO cliente);
}
