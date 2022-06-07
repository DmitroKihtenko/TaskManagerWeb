package ua.edu.sumdu.j2se.kikhtenkoDmytro.controllers;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.exceptions.ServiceException;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.Calendar;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.pojo.DatetimeRange;
import ua.edu.sumdu.j2se.kikhtenkoDmytro.service.CalendarService;

@RestController
public class CalendarController {
    private CalendarService calendarService;

    public CalendarController(@NonNull CalendarService
                                      calendarService) {
        setCalendarService(calendarService);
    }

    @NonNull
    public CalendarService getCalendarService() {
        return calendarService;
    }

    public void setCalendarService(@NonNull CalendarService
                                            authorityService) {
        this.calendarService = authorityService;
    }

    @RequestMapping(method = RequestMethod.GET,
            path = "/calendar",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Calendar get(@RequestBody DatetimeRange params)
            throws ServiceException {
        return calendarService.get(params);
    }
}
