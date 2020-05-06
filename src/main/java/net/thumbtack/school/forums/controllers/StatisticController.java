package net.thumbtack.school.forums.controllers;

import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticController {

    private StatisticsService statisticsService;

    @Autowired
    public StatisticController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping(value = "/forums/count", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase getStatisticForumCountMessages(@RequestParam(value = "id", required = false) Integer id,
                                                       @CookieValue(value = "JAVASESSIONID") String cookie,
                                                       @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                                       @RequestParam(value = "limit", required = false, defaultValue = "100") int limit) throws ForumException {
        return statisticsService.getCountMessages(cookie, id, offset, limit);
    }

    @GetMapping(value = "/messages/rating", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase getStatisticForumMessagesByRating(@RequestParam(value = "id", required = false) Integer id,
                                                          @CookieValue(value = "JAVASESSIONID") String cookie,
                                                          @RequestParam(value = "onlyComments", required = false, defaultValue = "false") boolean onlyComments,
                                                          @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                                          @RequestParam(value = "limit", required = false, defaultValue = "100") int limit) throws ForumException {
        return statisticsService.getListMessagesByRating(cookie, id, onlyComments, offset, limit);
    }

    @GetMapping(value = "/users/rating", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseBase getStatisticForumUsersByRating(@RequestParam(value = "id", required = false) Integer id,
                                                       @CookieValue(value = "JAVASESSIONID") String cookie,
                                                       @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
                                                       @RequestParam(value = "limit", required = false, defaultValue = "100") int limit) throws ForumException {
        return statisticsService.getListUsersByRating(cookie, id, offset, limit);
    }
}
