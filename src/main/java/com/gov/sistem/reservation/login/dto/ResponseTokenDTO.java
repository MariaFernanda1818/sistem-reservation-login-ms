package com.gov.sistem.reservation.login.dto;

import com.gov.sistem.reservation.commons.dto.ClienteDTO;
import lombok.Data;

@Data
public class ResponseTokenDTO {

    private String token;

    private ClienteDTO cliente;

}
