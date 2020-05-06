package net.thumbtack.school.forums.controllers;

import net.thumbtack.school.forums.dto.request.forums.CreateForumRequest;
import net.thumbtack.school.forums.dto.request.messages.CreateMessageRequest;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.types.Order;
import net.thumbtack.school.forums.service.ForumService;
import net.thumbtack.school.forums.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/forums")
public class ForumsController {
    private ForumService forumService;
    private MessageService messageService;

    @Autowired
    public ForumsController(ForumService forumService, MessageService messageService) {
        this.forumService = forumService;
        this.messageService = messageService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase createForum(@CookieValue(value = "JAVASESSIONID") String cookie, @RequestBody @Valid CreateForumRequest request) throws ForumException {
        return forumService.createForum(cookie, request);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase deleteForum(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie) throws ForumException {
        return forumService.deleteForum(cookie, id);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase getForum(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie) throws ForumException {
        return forumService.getForum(id, cookie);
    }

    @PostMapping(value = "/{id}/messages", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase createMessage(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie, @RequestBody @Valid CreateMessageRequest request) throws ForumException {
        return messageService.createMessage(id, cookie, request);
    }

    @GetMapping(value = "/{id}/messages", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase getMessagesOnForum(@PathVariable("id") int id,
                                           @CookieValue(value = "JAVASESSIONID") String cookie,
                                           @RequestParam(value = "allversions", required = false, defaultValue = "false") boolean allversions,
                                           @RequestParam(value = "nocomments", required = false, defaultValue = "false") boolean nocomments,
                                           @RequestParam(value = "unpublished", required = false, defaultValue = "false") boolean unpublished,
                                           @RequestParam(value = "order", required = false, defaultValue = "ASC") Order order,
                                           @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                           @RequestParam(value = "limit", required = false, defaultValue = "100") int limit) throws ForumException {
        return forumService.getForumMessages(id, cookie, allversions, nocomments, unpublished, order, offset, limit);
    }

}
