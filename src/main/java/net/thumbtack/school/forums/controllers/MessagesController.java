package net.thumbtack.school.forums.controllers;

import net.thumbtack.school.forums.dto.request.messages.*;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.types.Order;
import net.thumbtack.school.forums.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/messages")
public class MessagesController {
    @Autowired
    private MessageService messageService;

    @PostMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase createComment(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie, @RequestBody @Valid AddCommentRequest request) throws ForumException {
        return messageService.createComment(id, cookie, request);
    }

    @PostMapping(value = "/{id}/priority", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase changePriority(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie, @RequestBody @Valid ChangePriorityRequest request) throws ForumException {
        return messageService.changePriority(id, cookie, request);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase deleteMessageOrComment(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie) throws ForumException {
        return messageService.deleteMessageOrComment(id, cookie);
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase changeMessageOrComment(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie, @RequestBody @Valid ChangeMessageRequest request) throws ForumException {
        return messageService.changeMessage(id, cookie, request);
    }


    @PutMapping(value = "/{id}/up", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase convertCommentToMessage(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie, @RequestBody @Valid CommentIntoMessageRequest request) throws ForumException {
        return messageService.convertCommentToMessage(id, cookie, request);
    }

    @PutMapping(value = "/{id}/publish", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase publishMessage(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie, @RequestBody @Valid PublishMessageRequest request) throws ForumException {
        return messageService.publishMessage(id, cookie, request);
    }

    @PostMapping(value = "/{id}/rating", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase addRating(@PathVariable("id") int id, @CookieValue(value = "JAVASESSIONID") String cookie, @RequestBody @Valid AddRatingOnMessageRequest request) throws ForumException {
        return messageService.addRating(id, cookie, request);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase getMessage(@PathVariable("id") int id,
                                   @CookieValue(value = "JAVASESSIONID") String cookie,
                                   @RequestParam(value = "allversions", required = false, defaultValue = "false") boolean allversions,
                                   @RequestParam(value = "nocomments", required = false, defaultValue = "false") boolean nocomments,
                                   @RequestParam(value = "unpublished", required = false, defaultValue = "false") boolean unpublished,
                                   @RequestParam(value = "order", required = false, defaultValue = "ASC") Order order) throws ForumException {
        return messageService.getMessage(id, cookie, allversions, nocomments, unpublished, order);
    }

}
