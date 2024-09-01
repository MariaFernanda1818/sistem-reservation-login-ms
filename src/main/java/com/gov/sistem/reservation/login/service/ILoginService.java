package com.gov.sistem.reservation.login.service;

import com.gov.sistem.reservation.commons.dto.ClienteDTO;
import com.gov.sistem.reservation.login.dto.RespuestaGeneralDTO;

import java.security.NoSuchAlgorithmException;

public interface ILoginService {

    RespuestaGeneralDTO login(ClienteDTO cliente) throws NoSuchAlgorithmException;

}
