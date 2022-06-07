package ua.edu.sumdu.j2se.kikhtenkoDmytro.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.InfoResponse;

@RestController
public class HealthController {
    @RequestMapping(value = "/health",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public InfoResponse health() {
        return new InfoResponse("Server is available");
    }
}
