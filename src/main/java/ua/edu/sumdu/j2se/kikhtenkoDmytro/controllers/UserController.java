package ua.edu.sumdu.j2se.kikhtenkoDmytro.controllers;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Authorization;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.UserData;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.InfoResponse;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.SearchResult;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.UserService;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.util.CastAssertions;

import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    public UserController(@NonNull UserService userService) {
        setUserService(userService);
    }

    @NonNull
    public UserService getUserService() {
        return userService;
    }

    public void setUserService(@NonNull UserService service) {
        this.userService = service;
    }

    @PreAuthorize("hasAuthority('USERS_READ')")
    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchResult<ArrayList<UserData>> search(
            @RequestParam(value = "searchRegex",
                    defaultValue = "") String searchRegex,
            @RequestParam(value = "amount",
                    defaultValue = "50") String amount,
            @RequestParam(value = "from",
                    defaultValue = "0") String from)
            throws ServiceException {
        return userService.search(
                searchRegex,
                CastAssertions.stringToInt(amount, "Search amount"),
                CastAssertions.stringToInt(from, "From value")
        );
    }

    @PreAuthorize("hasAuthority('USERS_READ')")
    @RequestMapping(method = RequestMethod.GET,
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserData get(
            @PathVariable(value = "id") Integer id)
            throws ServiceException {
        return userService.get(id, false, true);
    }

    @PreAuthorize("hasAuthority('USERS_EDIT')")
    @RequestMapping(method = RequestMethod.DELETE,
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public InfoResponse delete(
            @PathVariable(value = "id") Integer id)
            throws ServiceException {
        userService.delete(id);
        return new InfoResponse("User successfully deleted");
    }

    @PreAuthorize("hasAuthority('USERS_EDIT')")
    @RequestMapping(method = RequestMethod.PATCH,
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public InfoResponse update(
            @PathVariable Integer id,
            @RequestBody Authorization user)
            throws ServiceException {
        userService.update(id, user);
        return new InfoResponse("User successfully updated");
    }

    @PreAuthorize("hasAuthority('USERS_EDIT')")
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public InfoResponse add(@RequestBody Authorization user)
            throws ServiceException {
        userService.add(user);
        return new InfoResponse("User successfully added");
    }
}
