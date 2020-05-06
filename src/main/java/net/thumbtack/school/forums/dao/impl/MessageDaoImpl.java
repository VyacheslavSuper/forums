package net.thumbtack.school.forums.dao.impl;

import net.thumbtack.school.forums.dao.MessageDao;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.message.MessageBody;
import net.thumbtack.school.forums.model.message.MessageHeader;
import net.thumbtack.school.forums.model.message.MessageItem;
import net.thumbtack.school.forums.model.message.MessageRating;
import net.thumbtack.school.forums.model.types.MessageState;
import net.thumbtack.school.forums.model.types.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class MessageDaoImpl extends BaseDaoImpl implements MessageDao {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public MessageHeader insertMessage(MessageHeader header) throws ForumException {
        LOGGER.debug("DAO insert MessageHeader {} ", header);
        try {
            messageMapper.insertHeader(header);
            messageMapper.insertItem(header, header.getRoot());
            messageMapper.insertBody(header.getRoot(), header.getRoot().getMessageBodyList().get(0));
            if (!header.getMessageTags().isEmpty()) {
                messageMapper.insertTags(header, header.getMessageTags());
            }
        } catch (RuntimeException ex) {
            LOGGER.info("Can't insert MessageHeader {} , {}", header, ex);
            throw new ForumException();
        }
        return header;
    }

    @Override
    public MessageItem insertComment(MessageHeader header, MessageItem message) throws ForumException {
        LOGGER.debug("DAO insert MessageItem {} ", message);
        try {
            messageMapper.insertItem(header, message);
            messageMapper.insertBody(message, message.getMessageBodyList().get(0));
        } catch (RuntimeException ex) {
            LOGGER.info("Can't insert MessageItem {}, {}", message, ex);
            throw new ForumException();
        }
        return message;
    }

    @Override
    public MessageBody insertBody(MessageItem message, MessageBody body) throws ForumException {
        LOGGER.debug("DAO insert MessageBody {} ", body);
        try {
            messageMapper.insertBody(message, body);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't insert MessageBody {} , {}", message, body, ex);
            throw new ForumException();
        }
        return body;
    }

    @Override
    public MessageHeader getHeaderById(int id) throws ForumException {
        LOGGER.debug("DAO get MessageHeader by Id {}", id);
        try {
            return messageMapper.getHeaderById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get MessageHeader by id {} , {}", id, ex);
            throw new ForumException();
        }
    }

    @Override
    public MessageItem getItemById(int id) throws ForumException {
        LOGGER.debug("DAO get MessageItem by Id {}", id);
        try {
            return messageMapper.getItemById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get MessageItem by id {} , {}", id, ex);
            throw new ForumException(id, ForumErrorCode.MESSAGE_NOT_FOUND);
        }
    }

    @Override
    public Boolean checkMessageItemById(int id) throws ForumException {
        LOGGER.debug("DAO check MessageItem by Id {}", id);
        try {
            return messageMapper.checkMessageItemById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't check MessageItem by id {} , {}", id, ex);
            throw new ForumException(id, ForumErrorCode.MESSAGE_NOT_FOUND);
        }
    }

    @Override
    public List<MessageItem> getListMessageItemByRoot(MessageItem message, Order order) throws ForumException {
        LOGGER.debug("DAO get all MessageItems");
        try {
            return messageMapper.getListMessageItemByRootWithParam(message, order);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get MessageItems by Root {} , {}", message, ex);
            throw new ForumException();
        }
    }

    @Override
    public List<MessageHeader> getAllHeaders() throws ForumException {
        LOGGER.debug("DAO get all Message");
        try {
            return messageMapper.getAllHeaders();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all MessageHeader ", ex);
            throw new ForumException();
        }
    }

    @Override
    public List<MessageHeader> getAllHeaders(Integer limit, Integer offset) throws ForumException {
        LOGGER.debug("DAO get all MessageHeader");
        try {
            return messageMapper.getAllHeadersWithParams(limit, offset);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all MessageHeader {},{}", limit, offset, ex);
            throw new ForumException();
        }
    }

    @Override
    public List<MessageItem> getAllMessageItem() throws ForumException {
        LOGGER.debug("DAO get all MessageItems");
        try {
            return messageMapper.getAllMessageItems();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all MessageItems ", ex);
            throw new ForumException();
        }
    }

    @Override
    public List<MessageItem> getAllMessageItem(Integer limit, Integer offset) throws ForumException {
        LOGGER.debug("DAO get all MessageItems");
        try {
            return messageMapper.getAllMessageItemsWithParams(limit, offset);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all MessageItems {},{}", limit, offset, ex);
            throw new ForumException();
        }
    }

    @Override
    public List<MessageHeader> getByForum(Forum forum, Integer limit, Integer offset, Order order) throws ForumException {
        LOGGER.debug("DAO get Messages by Forum");
        try {
            return messageMapper.getListByForumWithParams(forum, limit, offset, order);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all Message {},{},{},{}", forum, limit, offset, ex);
            throw new ForumException();
        }
    }

    @Override
    public List<MessageHeader> getByForum(Forum forum) throws ForumException {
        LOGGER.debug("DAO get Messages by Forum");
        try {
            return messageMapper.getListByForum(forum);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all Message {} ", forum, ex);
            throw new ForumException();
        }
    }

    @Override
    public MessageHeader replaceTagsOnMessage(MessageHeader header) throws ForumException {
        LOGGER.debug("DAO del and insert tags on Message");
        try {
            messageMapper.deleteTagsOnMessageHeader(header);
            if (!header.getMessageTags().isEmpty()) {
                messageMapper.insertTags(header, header.getMessageTags());
            }
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete and insert tags on Message {}, {}", header, ex);
            throw new ForumException();
        }
        return header;
    }

    @Override
    public void convertCommentToMessageAndUpdate(MessageHeader header) throws ForumException {
        LOGGER.debug("DAO conversion Comment into Message");
        try {
            messageMapper.insertHeader(header);
            messageMapper.conversionCommentIntoMessage(header, header.getRoot());
            if (!header.getMessageTags().isEmpty()) {
                messageMapper.insertTags(header, header.getMessageTags());
            }
        } catch (RuntimeException ex) {
            LOGGER.info("Can't conversion Comment into Message {}, {}", header, ex);
            throw new ForumException();
        }
    }

    @Override
    public void publishMessageBody(MessageItem message) throws ForumException {
        LOGGER.debug("DAO update State in Message");
        try {
            messageMapper.deleteMessageBody(message.getMessageBodyList().get(0));
            messageMapper.insertBody(message, new MessageBody(MessageState.PUBLISHED, message.getMessageBodyList().get(0).getBody()));
        } catch (RuntimeException ex) {
            LOGGER.info("Can't update State in Message {}, {}", message, ex);
            throw new ForumException();
        }
    }

    @Override
    public boolean checkRatingUserOnMessage(MessageItem message, User user) throws ForumException {
        LOGGER.debug("DAO check rating User on Message");
        try {
            return messageMapper.checkRatingUserOnMessage(message, user);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't check rating User on Message {},{},{}", message, user, ex);
            throw new ForumException();
        }
    }

    @Override
    public MessageRating addRatingOnMessage(MessageItem message, MessageRating messageRating) throws ForumException {
        LOGGER.debug("DAO add rating on MessageItem");
        try {
            messageMapper.addMessageRatingOnMessageItem(message, messageRating);
        } catch (RuntimeException ignored) {
            try {
                updateRatingOnMessage(message, messageRating);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't add rating on MessageItem {}, {}, {}", message, messageRating, ex);
                throw new ForumException();
            }
        }
        return messageRating;
    }

    @Override
    public MessageRating updateRatingOnMessage(MessageItem message, MessageRating messageRating) throws ForumException {
        LOGGER.debug("DAO update rating on Message");
        try {
            messageMapper.updateRatingOnMessage(message, messageRating.getUser(), messageRating.getRating());
        } catch (RuntimeException ex) {
            LOGGER.info("Can't update rating on Message {}, {}, {}", message, messageRating, ex);
            throw new ForumException();
        }
        return messageRating;
    }

    @Override
    public void delRatingOnMessage(MessageItem message, User user) throws ForumException {
        LOGGER.debug("DAO del rating on Message");
        try {
            messageMapper.delRatingOnMessage(message, user);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't del rating on Message {}, {}, {}", message, user, ex);
            throw new ForumException();
        }
    }

    @Override
    public void updateHeader(MessageHeader header) throws ForumException {
        LOGGER.debug("DAO update MessageHeader");
        try {
            messageMapper.updateHeader(header);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't update MessageHeader {} , {}", header, ex);
            throw new ForumException();
        }
    }

    @Override
    public void updateItem(MessageItem message) throws ForumException {
        LOGGER.debug("DAO update MessageHeader");
        try {
            messageMapper.updateItem(message);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't update MessageItem {} , {}", message, ex);
            throw new ForumException();
        }
    }

    @Override
    public void updateBody(MessageBody body) throws ForumException {
        LOGGER.debug("DAO update MessageBody");
        try {
            messageMapper.updateBody(body);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't update MessageBody {} , {}", body, ex);
            throw new ForumException();
        }
    }

    @Override
    public void deleteHeader(MessageHeader message) throws ForumException {
        LOGGER.debug("DAO delete MessageHeader");
        try {
            messageMapper.deleteMessageHeader(message);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete MessageHeader {} , {}", message, ex);
            throw new ForumException();
        }
    }

    @Override
    public void deleteItem(MessageItem message) throws ForumException {
        LOGGER.debug("DAO delete MessageItem");
        try {
            messageMapper.deleteMessageItem(message);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete MessageItem {} , {}", message, ex);
            throw new ForumException();
        }
    }

    @Override
    public void deleteBody(MessageBody body) throws ForumException {
        LOGGER.debug("DAO delete MessageBody");
        try {
            messageMapper.deleteMessageBody(body);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete MessageBody {} , {}", body, ex);
            throw new ForumException();
        }
    }
}
