package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.ApplicationProperties;
import net.thumbtack.school.forums.dao.*;
import net.thumbtack.school.forums.dto.request.users.LoginUserRequest;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.dto.response.message.FullInfoCommentResponse;
import net.thumbtack.school.forums.dto.response.message.FullInfoMessageResponse;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.mapstruct.ForumMapStruct;
import net.thumbtack.school.forums.mapstruct.MessageMapStruct;
import net.thumbtack.school.forums.mapstruct.UserMapStruct;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.Session;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.message.MessageBody;
import net.thumbtack.school.forums.model.message.MessageHeader;
import net.thumbtack.school.forums.model.message.MessageItem;
import net.thumbtack.school.forums.model.message.MessageTag;
import net.thumbtack.school.forums.model.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Transactional(rollbackFor = ForumException.class)
@Service
public class ServiceBase {

    @Autowired
    protected ApplicationProperties properties;
    @Autowired
    protected UserDao userDao;
    @Autowired
    protected SessionDao sessionDao;
    @Autowired
    protected ForumDao forumDao;
    @Autowired
    protected MessageDao messageDao;
    @Autowired
    protected ResponseDao responseDao;

    @Autowired
    protected UserMapStruct userMapStruct;
    @Autowired
    protected ForumMapStruct forumMapStruct;
    @Autowired
    protected MessageMapStruct messageMapStruct;

    protected ResponseBase login(String cookie, User user) throws ForumException {
        sessionDao.insert(new Session(cookie, user));
        //return UserResponse.toDto(user);
        return userMapStruct.userToUserResponse(user);
    }

    protected MessageHeader getMessageHeaderById(int id) throws ForumException {
        MessageHeader messageHeader = messageDao.getHeaderById(id);
        if (messageHeader == null) {
            throw new ForumException(id, ForumErrorCode.MESSAGE_NOT_FOUND);
        }
        return messageHeader;
    }

    protected MessageItem getMessageItemById(int id) throws ForumException {
        MessageItem messageItem = messageDao.getItemById(id);
        if (messageItem == null) {
            throw new ForumException(id, ForumErrorCode.MESSAGE_NOT_FOUND);
        }
        return messageItem;
    }

    protected User getUserByLogin(String login) throws ForumException {
        User user = userDao.getByLogin(login);
        if (user == null) {
            throw new ForumException(login, ForumErrorCode.LOGIN_NOT_FOUND);
        }
        return user;
    }

    protected User getUserById(int id) throws ForumException {
        User user = userDao.getById(id);
        if (user == null) {
            throw new ForumException(id, ForumErrorCode.USER_ID_NOT_FOUND);
        }
        return user;
    }

    protected Session getSessionByLogin(String login) throws ForumException {
        Session session = sessionDao.getByLogin(login);
        if (session == null) {
            throw new ForumException(login, ForumErrorCode.LOGIN_NOT_FOUND);
        }
        return session;
    }

    protected Forum getForumById(int id) throws ForumException {
        Forum forum = forumDao.getById(id);
        if (forum == null) {
            throw new ForumException(id, ForumErrorCode.FORUM_NOT_FOUND);
        }
        return forum;
    }

    protected Session getSessionByCookie(String cookie) throws ForumException {
        Session session = sessionDao.getByCookie(cookie);
        if (session == null) {
            throw new ForumException(ForumErrorCode.USER_UNAUTHORIZED);
        }
        return session;
    }

    protected User getUserByCookie(String cookie) throws ForumException {
        Session session = getSessionByCookie(cookie);
        checkerUserOnDeleted(session.getUser());
        return session.getUser();
    }

    protected boolean isForumAuthor(MessageItem message, User user) {
        return isForumAuthor(message.getMessageHeader().getForum(), user);
    }

    protected boolean isForumAuthor(Forum forum, User user) {
        return forum.getUser().equals(user);
    }

    protected boolean isGetUserByLogin(String login) throws ForumException {
        return userDao.checkUserByLogin(login);
    }

    protected boolean isGetUserById(int id) throws ForumException {
        return userDao.checkUserById(id);
    }

    protected boolean isGetSessionByLogin(String login) throws ForumException {
        return sessionDao.checkSessionByLogin(login);
    }

    protected boolean isGetSessionByCookie(String cookie) throws ForumException {
        return sessionDao.checkSessionByCookie(cookie);
    }

    protected boolean isGetMessageById(int id) throws ForumException {
        return messageDao.checkMessageItemById(id);
    }

    protected boolean isGetRatingMessage(MessageItem message, User user) throws ForumException {
        return messageDao.checkRatingUserOnMessage(message, user);
    }

    protected boolean isUserDeleted(User user) {
        return user.getDeleted();
    }

    protected boolean isUserBanned(User user) {
        return user.getRestrictionType() != RestrictionType.FULL;
    }

    protected boolean isUserSuper(User user) {
        return user.getUserType() == UserType.SUPERUSER;
    }

    protected boolean isAuthor(MessageItem message, User user) {
        return message.getUser().equals(user);
    }

    protected boolean isPublished(MessageItem message) {
        return isPublished(message.getMessageBodyList().get(0));
    }

    protected boolean isPublished(MessageBody body) {
        return body.getState().equals(MessageState.PUBLISHED);
    }

    protected boolean isForumModerated(MessageItem message) {
        return isForumModerated(message.getMessageHeader().getForum());
    }

    protected boolean isForumModerated(Forum forum) {
        return forum.getForumType().equals(ForumType.MODERATED);
    }

    protected void checkerUserOnDeleted(User user) throws ForumException {
        if (isUserDeleted(user)) {
            throw new ForumException(ForumErrorCode.USER_DELETED);
        }
    }

    protected void checkerUserOnBanned(User user) throws ForumException {
        if (isUserBanned(user)) {
            throw new ForumException(ForumErrorCode.USER_BANNED);
        }
    }

    protected void checkEnoughRightsForum(MessageItem message, User user) throws ForumException {
        checkEnoughRightsForum(message.getMessageHeader().getForum(), user);
    }

    protected void checkEnoughRightsForum(Forum forum, User user) throws ForumException {
        if (!isForumAuthor(forum, user) && !isUserSuper(user)) {
            throw new ForumException(ForumErrorCode.NOT_ENOUGH_RIGHTS);
        }
    }

    protected void checkEnoughRightsMessage(MessageItem message, User user) throws ForumException {
        if (!isAuthor(message, user) && !isUserSuper(user)) {
            throw new ForumException(ForumErrorCode.NOT_ENOUGH_RIGHTS);
        }
    }

    protected void checkEnoughRightsUser(User user) throws ForumException {
        if (!isUserSuper(user)) {
            throw new ForumException(ForumErrorCode.NOT_ENOUGH_RIGHTS);
        }
    }

    protected void checkAlreadySuperUser(User user) throws ForumException {
        if (isUserSuper(user)) {
            throw new ForumException(ForumErrorCode.ALREADY_SUPERUSER);
        }
    }

    protected void checkForumOnReadOnly(Forum forum) throws ForumException {
        if (forum.getForumType().equals(ForumType.MODERATED) && forum.getReadonly()) {
            throw new ForumException(forum.getId(), ForumErrorCode.FORUM_IS_READONLY);
        }
    }

    protected Boolean checkForumById(int id) throws ForumException {
        return forumDao.checkForumById(id);
    }

    protected User checkBeforeLogin(LoginUserRequest request) throws ForumException {
        User user = getUserByLogin(request.getLogin());
        if (!user.getPassword().equals(request.getPassword())) {
            throw new ForumException(ForumErrorCode.LOGIN_OR_PASSWORD_INVALID);
        }
        return user;
    }

    protected void sendMail(User user, String message) {
        Logger LOGGER = LoggerFactory.getLogger(this.getClass());
        LOGGER.info("send on {} letter {}", user.getEmail(), message);
    }

    protected FullInfoMessageResponse getFullInfoMessageResponse(MessageItem message, boolean allversions, boolean nocomments, boolean unpublished, Order order) throws ForumException {
        List<MessageBody> messageBodyList = new ArrayList<>();

        addUnpublishedBodyInList(message, unpublished, messageBodyList);

        addPublishedBodyInList(message, allversions, messageBodyList);

        List<FullInfoCommentResponse> commentsReady = addFullInFoCommentInList(message, allversions, nocomments, unpublished, order);

        if (messageBodyList.isEmpty()) {
            return null;
        }

        List<String> bodies = getStringBodiesFromMessageBodyList(messageBodyList);

        List<String> tags = getStringTagsFromMessage(message);
        FullInfoMessageResponse fe = messageMapStruct.messageToFullInfoMessageResponse(message, bodies, tags, commentsReady);
        return messageMapStruct.messageToFullInfoMessageResponse(message, bodies, tags, commentsReady);
    }


    protected FullInfoCommentResponse getFullInfoCommentResponse(MessageItem message, boolean allversions, boolean nocomments, boolean unpublished, Order order) throws ForumException {
        List<MessageBody> messageBodyList = new ArrayList<>();

        addUnpublishedBodyInList(message, unpublished, messageBodyList);

        addPublishedBodyInList(message, allversions, messageBodyList);

        List<FullInfoCommentResponse> commentsReady = addFullInFoCommentInList(message, allversions, nocomments, unpublished, order);

        if (messageBodyList.isEmpty()) {
            return null;
        }

        List<String> bodies = messageBodyList.stream().map(MessageBody::getBody).collect(Collectors.toList());

        return messageMapStruct.messageToFullInfoCommentResponse(message, bodies, commentsReady);
    }

    private List<String> getStringBodiesFromMessageBodyList(List<MessageBody> messageBodyList) {
        return messageBodyList
                .stream()
                .map(MessageBody::getBody)
                .collect(Collectors.toList());
    }

    private List<String> getStringTagsFromMessage(MessageItem message) {
        return message.getMessageHeader()
                .getMessageTags()
                .stream()
                .map(MessageTag::getTag)
                .collect(Collectors.toList());
    }

    private void addUnpublishedBodyInList(MessageItem message, boolean unpublished, List<MessageBody> messageBodyList) {
        if (unpublished && isForumModerated(message)) {
            message.getMessageBodyList()
                    .stream()
                    .filter(body -> !isPublished(body))
                    .findFirst()
                    .ifPresent(body -> {
                        body.setBody("[" + MessageState.UNPUBLISHED + "]" + body.getBody());
                        messageBodyList.add(body);
                    });
        }
    }

    private void addPublishedBodyInList(MessageItem message, boolean allversions, List<MessageBody> messageBodyList) {
        Stream<MessageBody> streamFiltedPublished = message.getMessageBodyList()
                .stream()
                .filter(this::isPublished);
        if (allversions) {
            streamFiltedPublished
                    .forEach(messageBodyList::add);
        } else {
            streamFiltedPublished
                    .findFirst()
                    .ifPresent(messageBodyList::add);
        }
    }

    private List<FullInfoCommentResponse> addFullInFoCommentInList(MessageItem message, boolean allversions, boolean nocomments, boolean unpublished, Order order) throws ForumException {
        List<FullInfoCommentResponse> commentsReady = new ArrayList<>();
        if (!nocomments && !message.getMessages().isEmpty()) {
            for (MessageItem comment : messageDao.getListMessageItemByRoot(message, order)) {
                commentsReady.add(getFullInfoCommentResponse(comment, allversions, nocomments, unpublished, order));
            }
        }
        return commentsReady;
    }
}
