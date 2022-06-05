package ua.edu.sumdu.j2se.kikhtenkoDmytro.controllers;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Authentication;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.InfoResponse;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Token;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.UserService;

@RestController
public class AuthenticationController {
    private UserService userService;

    public AuthenticationController(@NonNull UserService accessService) {
        this.userService = accessService;
    }

    @NonNull
    public UserService getUserService() {
        return userService;
    }

    public void setUserService(@NonNull UserService accessService) {
        this.userService = accessService;
    }

    @RequestMapping(method = RequestMethod.POST,
            path = "/signIn",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Token signIn(@RequestBody Authentication loginData)
            throws ServiceException {
        return userService.createToken(
                userService.authorize(loginData));
    }

    @RequestMapping(method = RequestMethod.GET,
            path = "/auth",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public org.springframework.security.core.Authentication get()
            throws ServiceException {
        return userService.getCurrentAuthentication();
    }

    @PreAuthorize("hasAuthority('CREDS_EDIT')")
    @RequestMapping(method = RequestMethod.PATCH,
            path = "/auth",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public InfoResponse update(@RequestBody Authentication loginData)
            throws ServiceException {
        userService.update(userService.get(
                userService.getCurrentAuthentication().getName(),
                false, true).getId(), loginData);
        return new InfoResponse("Successfully updated. You must " +
                "logout to apply changes");
    }
}
