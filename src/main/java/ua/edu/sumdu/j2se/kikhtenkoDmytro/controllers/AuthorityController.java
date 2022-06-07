package ua.edu.sumdu.j2se.kikhtenkoDmytro.controllers;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.AuthorityData;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.AuthorityService;

import java.util.ArrayList;

@RestController
@RequestMapping("/authorities")
public class AuthorityController {
    private AuthorityService authorityService;

    public AuthorityController(@NonNull AuthorityService
                                       authorityService) {
        setAuthorityService(authorityService);
    }

    @NonNull
    public AuthorityService getAuthorityService() {
        return authorityService;
    }

    public void setAuthorityService(@NonNull AuthorityService
                                            authorityService) {
        this.authorityService = authorityService;
    }

    @RequestMapping(method = RequestMethod.GET,
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthorityData get(@PathVariable int id) {
        return authorityService.get(id);
    }

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<AuthorityData> get() {
        return authorityService.get();
    }
}
