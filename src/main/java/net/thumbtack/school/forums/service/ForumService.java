package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.request.forums.CreateForumRequest;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.dto.response.message.FullInfoMessageResponse;
import net.thumbtack.school.forums.dto.response.message.ListFullInfoMessageResponse;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.message.MessageHeader;
import net.thumbtack.school.forums.model.types.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForumService extends ServiceBase {


    public ResponseBase createForum(String cookie, CreateForumRequest request) throws ForumException {
        User user = getUserByCookie(cookie);
        checkerUserOnBanned(user);
        Forum forum = forumDao.insert(new Forum(user, request.getTopic(), request.getForumType()));
        return forumMapStruct.forumToForumResponse(forum);
    }

    public ResponseBase deleteForum(String cookie, int idForum) throws ForumException {
        User user = getUserByCookie(cookie);
        Forum forum = getForumById(idForum);

        checkEnoughRightsForum(forum, user);

        if (!forum.getMessages().isEmpty() && isForumModerated(forum)) {
            forumDao.safeDelete(forum);
        } else {
            forumDao.delete(forum);
        }
        return new ResponseBase();
    }

    public ResponseBase getForum(int idForum, String cookie) throws ForumException {
        getUserByCookie(cookie);
        return forumMapStruct.forumToFullInfoForumResponse(getForumById(idForum));
    }


    public ResponseBase getForumMessages(int idForum, String cookie, boolean allversions, boolean nocomments, boolean unpublished, Order order, int offset, int limit) throws ForumException {
        User user = getUserByCookie(cookie);
        Forum forum = getForumById(idForum);
        if (unpublished) {
            checkEnoughRightsForum(forum, user);
        }
        List<FullInfoMessageResponse> list = new ArrayList<>();

        for (MessageHeader header : messageDao.getByForum(forum, limit, offset, order)) {
            list.add(getFullInfoMessageResponse(header.getRoot(), allversions, nocomments, unpublished, order));
        }

        return new ListFullInfoMessageResponse(list);
    }

}
