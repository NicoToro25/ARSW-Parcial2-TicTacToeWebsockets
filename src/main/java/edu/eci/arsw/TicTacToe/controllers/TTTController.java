package edu.eci.arsw.TicTacToe.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TTTController {
    @RequestMapping(
            value = "/status",
            method = RequestMethod.GET,
            produces = "application/json"
    )

    public String status() {
        return  "{\"status\":\"Greetings from Spring Boot. "
                + java.time.LocalDate.now() + ", "
                + java.time.LocalTime.now()
                + ". " + "The server is Running!\"}";
    }
}
