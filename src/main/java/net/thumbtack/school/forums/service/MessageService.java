package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.request.messages.*;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.dto.response.message.MessageResponse;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.message.*;
import net.thumbtack.school.forums.model.types.Decision;
import net.thumbtack.school.forums.model.types.MessageState;
import net.thumbtack.school.forums.model.types.Order;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService extends ServiceBase {

    public ResponseBase createMessage(int idForum, String cookie, CreateMessageRequest request) throws ForumException {
        User user = getUserByCookie(cookie);
        checkerUserOnBanned(user);
        Forum forum = getForumById(idForum);
        checkForumOnReadOnly(forum);

        MessageState messageState = getMessageState(forum, user);
        MessageBody messageBody = new MessageBody(messageState, request.getBody());
        List<MessageBody> messageBodyList = new ArrayList<>();
        messageBodyList.add(messageBody);

        MessageItem message = new MessageItem(LocalDateTime.now(), user, null, messageBodyList);
        List<MessageTag> messageTags = new ArrayList<>();

        request.getTags().forEach(tag -> messageTags.add(new MessageTag(tag)));
        MessageHeader header = new MessageHeader(request.getPriority(), request.getTopic(), forum, message, messageTags);
        messageDao.insertMessage(header);
        return new MessageResponse(message.getId(), messageState);
    }

    public ResponseBase createComment(int idMessage, String cookie, AddCommentRequest request) throws ForumException {
        User user = getUserByCookie(cookie);
        checkerUserOnBanned(user);
        MessageItem message = getMessageItemById(idMessage);
        checkForumOnReadOnly(message.getMessageHeader().getForum());
        if (message.getMessageBodyList().size() == 1
                && !isPublished(message)) {
            throw new ForumException(ForumErrorCode.MESSAGE_NOT_FOUND);
        }
        MessageState messageState = getMessageState(message.getMessageHeader().getForum(), user);
        MessageBody messageBody = new MessageBody(messageState, request.getBody());
        List<MessageBody> messageBodyList = new ArrayList<>();
        messageBodyList.add(messageBody);

        MessageItem messageItem = new MessageItem(LocalDateTime.now(), user, message, messageBodyList);
        messageDao.insertComment(message.getMessageHeader(), messageItem);

        return new MessageResponse(messageItem.getId(), messageItem.getMessageBodyList().get(0).getState());
    }

    public ResponseBase deleteMessageOrComment(int idMessage, String cookie) throws ForumException {
        User user = getUserByCookie(cookie);
        MessageItem message = getMessageItemById(idMessage);

        checkEnoughRightsMessage(message, user);

        if (!message.getMessages().isEmpty()) {
            throw new ForumException(idMessage, ForumErrorCode.DELETE_MESSAGE_IMPOSSIBLE);
        } else {
            deleteMessage(message);
        }
        return new ResponseBase();
    }

    public ResponseBase changeMessage(int idMessage, String cookie, ChangeMessageRequest request) throws ForumException {
        User user = getUserByCookie(cookie);
        checkerUserOnBanned(user);
        MessageItem message = getMessageItemById(idMessage);

        checkEnoughRightsMessage(message, user);

        MessageBody firstBody = message.getMessageBodyList().get(0);

        if (!isForumModerated(message)
                || isForumAuthor(message, user)
                || isUserSuper(user)) {
            if (isPublished(firstBody)) {
                return createBody(message, MessageState.PUBLISHED, request.getBody());
            } else {
                firstBody.setBody(request.getBody());
                firstBody.setState(MessageState.PUBLISHED);
                messageDao.updateBody(firstBody);
                return new MessageResponse(message.getId(), firstBody.getState());
            }
        } else {
            if (isPublished(firstBody)) {
                return createBody(message, MessageState.UNPUBLISHED, request.getBody());
            } else {
                firstBody.setBody(request.getBody());
                messageDao.updateBody(firstBody);
                return new MessageResponse(message.getId(), firstBody.getState());
            }
        }
    }

    public ResponseBase changePriority(int idMessage, String cookie, ChangePriorityRequest request) throws ForumException {
        User user = getUserByCookie(cookie);
        checkerUserOnBanned(user);
        MessageItem message = getMessageItemById(idMessage);

        checkEnoughRightsMessage(message, user);

        MessageHeader header = message.getMessageHeader();
        if (header.getPriority().equals(request.getPriority())) {
            throw new ForumException(ForumErrorCode.PRIORITY_MATCHES);
        }
        header.setPriority(request.getPriority());
        messageDao.updateHeader(header);
        return new ResponseBase();
    }

    public ResponseBase publishMessage(int idMessage, String cookie, PublishMessageRequest request) throws ForumException {
        User user = getUserByCookie(cookie);
        MessageItem message = getMessageItemById(idMessage);
        if (!isForumModerated(message)) {
            throw new ForumException(message.getId(), ForumErrorCode.FORUM_UNMODERATED);
        }

        checkEnoughRightsForum(message, user);

        if (request.getDecision().equals(Decision.YES)) {
            if (isPublished(message)) {
                throw new ForumException(idMessage, ForumErrorCode.MESSAGEBODY_FULL_PUBLISHED);
            }
            messageDao.publishMessageBody(message);
        } else {
            sendMail(message.getUser(), "Message unpublish");
            if (message.getMessageBodyList().size() > 1) {
                messageDao.deleteBody(message.getMessageBodyList().get(0));
            } else {
                deleteMessage(message);
            }
        }
        return new ResponseBase();
    }


    public ResponseBase addRating(int idMessage, String cookie, AddRatingOnMessageRequest request) throws ForumException {
        User user = getUserByCookie(cookie);
        MessageItem message = getMessageItemById(idMessage);

        if (isAuthor(message, user)) {
            throw new ForumException(ForumErrorCode.USER_MATCHES);
        }

        if (request.getValue() != null) {
            MessageRating messageRating = new MessageRating(user, request.getValue());
            messageDao.addRatingOnMessage(message, messageRating);
        } else {
            messageDao.delRatingOnMessage(message, user);
        }
        return new ResponseBase();
    }


    public ResponseBase convertCommentToMessage(int idMessage, String cookie, CommentIntoMessageRequest request) throws ForumException {
        User user = getUserByCookie(cookie);
        checkerUserOnBanned(user);
        MessageItem message = getMessageItemById(idMessage);

        checkEnoughRightsForum(message, user);

        if (message.getParent() == null) {
            throw new ForumException(idMessage, ForumErrorCode.CONVERSION_COMMENT_IMPOSSIBLE);
        }

        List<MessageTag> messageTags = new ArrayList<>();
        if (!request.getTags().isEmpty()) {
            request.getTags().forEach(tag -> messageTags.add(new MessageTag(tag)));
        }
        messageDao.convertCommentToMessageAndUpdate(new MessageHeader(request.getPriority(), request.getTopic(), message.getMessageHeader().getForum(), message, messageTags));
        return new MessageResponse(message.getId(), message.getMessageBodyList().get(0).getState());
    }


    public ResponseBase getMessage(int idMessage, String cookie, boolean allversions, boolean nocomments, boolean unpublished, Order order) throws ForumException {
        User user = getUserByCookie(cookie);
        MessageItem message = getMessageItemById(idMessage);
        if (unpublished) {
            checkEnoughRightsForum(message, user);
        }
        if (message.getParent() == null) {
            return getFullInfoMessageResponse(message, allversions, nocomments, unpublished, order);
        } else {
            return getFullInfoCommentResponse(message, allversions, nocomments, unpublished, order);
        }

    }

    private MessageState getMessageState(Forum forum, User user) {
        if (isForumAuthor(forum, user)
                || isUserSuper(user)
                || !isForumModerated(forum)) {
            return MessageState.PUBLISHED;
        }
        return MessageState.UNPUBLISHED;
    }

    private ResponseBase createBody(MessageItem message, MessageState state, String body) throws ForumException {
        MessageBody newBody = new MessageBody(state, body);
        messageDao.insertBody(message, newBody);
        return new MessageResponse(message.getId(), newBody.getState());
    }

    private void deleteMessage(MessageItem message) throws ForumException {
        if (message.getParent() == null) {
            messageDao.deleteHeader(message.getMessageHeader());
        } else {
            messageDao.deleteItem(message);
        }
    }

}
