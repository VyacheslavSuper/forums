package net.thumbtack.school.forums.controllers;

import net.thumbtack.school.forums.dto.request.users.LoginUserRequest;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.service.CookieService;
import net.thumbtack.school.forums.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/sessions")
public class SessionsController {

    private SessionService sessionService;
    private CookieService cookieService;

    @Autowired
    public SessionsController(SessionService sessionService, CookieService cookieService) {
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase login(@Valid @RequestBody LoginUserRequest request, HttpServletResponse response) throws ForumException {
        Cookie cookie = cookieService.createCookie();
        ResponseBase responseBase = sessionService.login(cookie.getValue(), request);
        response.addCookie(cookie);
        return responseBase;
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase logout(@CookieValue(value = "JAVASESSIONID") String cookie, HttpServletResponse response) throws ForumException {
        ResponseBase responseBase = sessionService.logout(cookie);
        response.addCookie(cookieService.deleteCookie());
        return responseBase;
    }

}
