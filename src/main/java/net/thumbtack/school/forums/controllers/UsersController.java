package net.thumbtack.school.forums.controllers;

import net.thumbtack.school.forums.dto.request.users.ChangePasswordRequest;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.service.CookieService;
import net.thumbtack.school.forums.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private UserService userService;
    private CookieService cookieService;

    @Autowired
    public UsersController(UserService userService, CookieService cookieService) {
        this.userService = userService;
        this.cookieService = cookieService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase registrationUser(@Valid @RequestBody RegisterUserRequest request, HttpServletResponse response) throws ForumException {
        Cookie cookie = cookieService.createCookie();
        ResponseBase responseBase = userService.register(cookie.getValue(), request);
        response.addCookie(cookie);
        return responseBase;
    }

    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase deleteUser(@CookieValue(value = "JAVASESSIONID") String cookie, HttpServletResponse response) throws ForumException {
        ResponseBase responseBase = userService.deleteUser(cookie);
        response.addCookie(cookieService.deleteCookie());
        return responseBase;
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase changePassword(@CookieValue(value = "JAVASESSIONID") String cookie, @RequestBody @Valid ChangePasswordRequest request, HttpServletResponse response) throws ForumException {
        return userService.changePassword(cookie, request);
    }

    @PutMapping(value = "/{id}/super", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase setSuperUser(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie) throws ForumException {
        return userService.setSuperUser(cookie, id);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase getUsers(@CookieValue(value = "JAVASESSIONID") String cookie) throws ForumException {
        return userService.getUsers(cookie);
    }

    @PostMapping(value = "/{id}/restrict", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase banUser(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie) throws ForumException {
        return userService.banUser(cookie, id);
    }
}
