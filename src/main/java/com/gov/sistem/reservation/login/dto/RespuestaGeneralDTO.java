package com.gov.sistem.reservation.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaGeneralDTO {

    @Builder.Default
    private Boolean error = false;

    private HttpStatus status;

    private String mensaje;

    private Object data;
}
