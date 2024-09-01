package com.gov.sistem.reservation.login.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
@RequiredArgsConstructor
public class DemoController {

    @PostMapping(value = "demo")
    public String welcome(){
        return "Bienvenido a un endpoint seguro";
    }

}
