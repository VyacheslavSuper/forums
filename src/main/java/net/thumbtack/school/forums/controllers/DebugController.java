package net.thumbtack.school.forums.controllers;

import net.thumbtack.school.forums.dto.request.RequestBase;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.service.DebugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/debug")
public class DebugController {
    private DebugService debugService;

    @Autowired
    public DebugController(DebugService debugService) {
        this.debugService = debugService;
    }

    @PostMapping(value = "/clear", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase clearAll(@RequestBody RequestBase request) throws ForumException {
        return debugService.clear(request);
    }
}
