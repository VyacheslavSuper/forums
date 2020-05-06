package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.dto.response.statistics.*;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticsService extends ServiceBase {

    public ResponseBase getCountMessages(String cookie, Integer idForum, int offset, int limit) throws ForumException {
        getUserByCookie(cookie);
        List<CountMessagesResponse> list = new ArrayList<>();
        Forum forum = null;
        if (idForum != null) {
            forum = getForumById(idForum);
        }
        responseDao
                .getListForumView(forum, limit, offset)
                .forEach(forumView -> list.add(forumMapStruct.forumViewToCountMessage(forumView)));
        return new ListCountMessagesResponse(list);
    }

    public ResponseBase getListMessagesByRating(String cookie, Integer idForum, boolean onlyComment, int offset, int limit) throws ForumException {
        getUserByCookie(cookie);
        List<RatingMessagesResponse> list = new ArrayList<>();
        Forum forum = null;
        if (idForum != null) {
            forum = getForumById(idForum);
        }
        responseDao
                .getListMessageViewByForum(forum, limit, offset, onlyComment)
                .forEach(messageView -> list.add(messageMapStruct.messageViewToRatingMessageResponse(messageView)));
        return new ListRatingMessagesResponse(list);
    }

    public ResponseBase getListUsersByRating(String cookie, Integer idForum, int offset, int limit) throws ForumException {
        getUserByCookie(cookie);
        List<RatingUserResponse> list = new ArrayList<>();
        Forum forum = null;
        if (idForum != null) {
            forum = getForumById(idForum);
        }
        responseDao.getListMessageViewByForumGroupByCreator(forum, limit, offset)
                .forEach(messageView -> list.add(messageMapStruct.messageViewToRatingUserResponse(messageView)));
        return new ListRatingUserResponse(list);
    }
}
