package ar.com.meli.cupon.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthcheckController {

    @GetMapping("/")
    public String healthCheck() {
        return "OK";
    }

}
