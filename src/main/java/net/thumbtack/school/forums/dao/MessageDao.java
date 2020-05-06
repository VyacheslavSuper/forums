package net.thumbtack.school.forums.dao;

import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.message.MessageBody;
import net.thumbtack.school.forums.model.message.MessageHeader;
import net.thumbtack.school.forums.model.message.MessageItem;
import net.thumbtack.school.forums.model.message.MessageRating;
import net.thumbtack.school.forums.model.types.Order;

import java.util.List;

public interface MessageDao {

    MessageHeader insertMessage(MessageHeader header) throws ForumException;

    MessageItem insertComment(MessageHeader header, MessageItem message) throws ForumException;

    MessageBody insertBody(MessageItem message, MessageBody body) throws ForumException;

    MessageHeader getHeaderById(int id) throws ForumException;

    MessageItem getItemById(int id) throws ForumException;

    Boolean checkMessageItemById(int id) throws ForumException;

    List<MessageItem> getListMessageItemByRoot(MessageItem message, Order order) throws ForumException;

    List<MessageHeader> getAllHeaders() throws ForumException;

    List<MessageHeader> getAllHeaders(Integer limit, Integer offset) throws ForumException;

    List<MessageItem> getAllMessageItem() throws ForumException;

    List<MessageItem> getAllMessageItem(Integer limit, Integer offset) throws ForumException;

    List<MessageHeader> getByForum(Forum forum, Integer limit, Integer offset, Order order) throws ForumException;

    List<MessageHeader> getByForum(Forum forum) throws ForumException;

    MessageHeader replaceTagsOnMessage(MessageHeader header) throws ForumException;

    void convertCommentToMessageAndUpdate(MessageHeader header) throws ForumException;

    void publishMessageBody(MessageItem message) throws ForumException;

    boolean checkRatingUserOnMessage(MessageItem message, User user) throws ForumException;

    MessageRating addRatingOnMessage(MessageItem message, MessageRating messageRating) throws ForumException;

    MessageRating updateRatingOnMessage(MessageItem message, MessageRating messageRating) throws ForumException;

    void delRatingOnMessage(MessageItem message, User user) throws ForumException;

    void updateHeader(MessageHeader header) throws ForumException;

    void updateItem(MessageItem message) throws ForumException;

    void updateBody(MessageBody body) throws ForumException;

    void deleteHeader(MessageHeader message) throws ForumException;

    void deleteItem(MessageItem message) throws ForumException;

    void deleteBody(MessageBody body) throws ForumException;
}
