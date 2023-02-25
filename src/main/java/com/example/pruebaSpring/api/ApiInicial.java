package com.example.pruebaSpring.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiInicial {

    @GetMapping("/b")
    public static double CalcularPayback(double a, double I, double b, double f  ){
        double PayBack;
        PayBack = (a + ((I-b)/f));
        return (int) PayBack;
    }
}
