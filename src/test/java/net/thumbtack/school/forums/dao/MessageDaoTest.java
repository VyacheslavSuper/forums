package net.thumbtack.school.forums.dao;

import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.message.*;
import net.thumbtack.school.forums.model.types.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MessageDaoTest extends DaoTestBase {

    @Test
    public void testFullMessageDao() throws ForumException {
        User user = new User("TestLogin4", "Mypassword23", "MyNames", "testemail", UserType.USER, LocalDate.now(), RestrictionType.FULL, 5, null);
        userDao.insert(user);
        Forum forum = new Forum(user, "MyTopic", ForumType.UNMODERATED);
        forumDao.insert(forum);

        MessageBody messageBody = new MessageBody(0, MessageState.PUBLISHED, "Hello world");

        List<MessageBody> messageBodyList = new ArrayList<>();
        messageBodyList.add(messageBody);

        List<MessageTag> tags = new ArrayList<>();
        tags.add(new MessageTag("test"));
        tags.add(new MessageTag("testHelloWorld"));
        MessageItem messageItem = new MessageItem(LocalDateTime.now(), user, null, messageBodyList);
        MessageHeader firstheader = new MessageHeader(MessagePriority.NORMAL, "TopicMessage", forum, messageItem, tags);
        messageDao.insertMessage(firstheader);

        MessageHeader result = messageDao.getHeaderById(firstheader.getId());

        Assert.assertEquals(1, userDao.getById(user.getId()).getMessages().size());
        Assert.assertEquals(1, messageDao.getAllHeaders().size());
        Forum resultforum = forumDao.getById(forum.getId());
        Assert.assertEquals(1, resultforum.getMessages().size());
        Assert.assertEquals(1, forumDao.getById(forum.getId()).getMessages().size());
        Assert.assertEquals(0, forumDao.getById(forum.getId()).getCountComments());

        // Assert.assertEquals(firstheader, result);
        Assert.assertEquals(forum, result.getForum());

        result.setPriority(MessagePriority.HIGH);
        result.setTopic("ModifedTopicMessage");

        messageDao.updateHeader(result);
        Assert.assertEquals(MessagePriority.HIGH, messageDao.getHeaderById(result.getId()).getPriority());
        Assert.assertEquals("ModifedTopicMessage", messageDao.getHeaderById(result.getId()).getTopic());

        result.getMessageTags().add(new MessageTag("JustTest"));

        messageDao.replaceTagsOnMessage(result);
        Assert.assertEquals(result, messageDao.getHeaderById(result.getId()));

        MessageBody messageBodycomment = new MessageBody(0, MessageState.UNPUBLISHED, "Hello world");

        List<MessageBody> messageBodyListcomment = new ArrayList<>();
        messageBodyListcomment.add(messageBodycomment);

        MessageItem messagecomment = new MessageItem(LocalDateTime.now(), user, messageItem, messageBodyListcomment);
        messageDao.insertComment(result, messagecomment);


        Assert.assertEquals(2, userDao.getById(user.getId()).getMessages().size());
        Assert.assertEquals(1, messageDao.getAllHeaders().size());
        Assert.assertTrue(messageDao.getAllHeaders(10, 1).isEmpty());
        Assert.assertEquals(2, messageDao.getAllMessageItem().size());
        Assert.assertEquals(1, messageDao.getAllMessageItem(10, 1).size());
        Assert.assertEquals(1, forumDao.getById(forum.getId()).getCountMessages());
        Assert.assertEquals(1, forumDao.getById(forum.getId()).getMessages().size());
        Assert.assertEquals(1, forumDao.getById(forum.getId()).getCountComments());
        MessageHeader messageHeadernew = new MessageHeader(MessagePriority.HIGH, "blabla", forum, messagecomment, new ArrayList<>());
        messageDao.convertCommentToMessageAndUpdate(messageHeadernew);
        Assert.assertEquals(2, userDao.getById(user.getId()).getMessages().size());
        Assert.assertEquals(2, messageDao.getAllHeaders().size());
        Assert.assertEquals(2, messageDao.getAllMessageItem().size());
        Assert.assertEquals(2, messageDao.getAllMessageItem(10, 0).size());
        Assert.assertEquals(2, forumDao.getById(forum.getId()).getCountMessages());
        Assert.assertEquals(2, forumDao.getById(forum.getId()).getMessages().size());
        Assert.assertEquals(0, forumDao.getById(forum.getId()).getCountComments());

        //add rating on first message
        messageDao.addRatingOnMessage(messagecomment, new MessageRating(user, 4));

        //check rating
        Double rating = messageDao.getItemById(messagecomment.getId()).getRating();
        Assert.assertTrue(rating != 0 && rating == 4.0);

        messageDao.updateRatingOnMessage(messagecomment, new MessageRating(user, 5));

        //update rating
        Double rating2 = messageDao.getItemById(messagecomment.getId()).getRating();
        Assert.assertTrue(rating2 != 0 && rating2 == 5.0);

        //check
        Assert.assertTrue(messageDao.checkRatingUserOnMessage(messagecomment, user));

        //del rating
        messageDao.delRatingOnMessage(messagecomment, user);

        Double rating3 = messageDao.getItemById(messagecomment.getId()).getRating();
        Assert.assertEquals(0.0, rating3, 0.0);

        //check
        Assert.assertFalse(messageDao.checkRatingUserOnMessage(messagecomment, user));

        //Удаление дерева удалит и последующее
        messageDao.deleteHeader(firstheader);

        Assert.assertEquals(1, userDao.getById(user.getId()).getMessages().size());
        Assert.assertEquals(1, messageDao.getAllHeaders().size());
        Assert.assertEquals(1, messageDao.getAllMessageItem().size());
        Assert.assertEquals(1, forumDao.getById(forum.getId()).getCountMessages());
        Assert.assertEquals(1, forumDao.getById(forum.getId()).getMessages().size());
    }

    @Test
    public void testBody() throws ForumException {
        User user = new User("TestLogin4", "Mypassword23", "MyNames", "testemail", UserType.USER, LocalDate.now(), RestrictionType.FULL, 5, null);
        userDao.insert(user);
        Forum forum = new Forum(user, "MyTopic", ForumType.UNMODERATED);
        forumDao.insert(forum);

        MessageBody messageBody = new MessageBody(0, MessageState.PUBLISHED, "Hello world");

        List<MessageBody> messageBodyList = new ArrayList<>();
        messageBodyList.add(messageBody);

        MessageItem messageItem = new MessageItem(LocalDateTime.now(), user, null, messageBodyList);
        MessageHeader firstheader = new MessageHeader(MessagePriority.NORMAL, "TopicMessage", forum, messageItem, new ArrayList<>());
        messageDao.insertMessage(firstheader);

        messageDao.insertBody(messageItem, new MessageBody(MessageState.PUBLISHED, "Hello  2"));
        messageDao.insertBody(messageItem, new MessageBody(MessageState.PUBLISHED, "Hello  3"));
        messageDao.insertBody(messageItem, new MessageBody(MessageState.PUBLISHED, "Hello  4"));
        messageDao.insertBody(messageItem, new MessageBody(MessageState.UNPUBLISHED, "Hello  5"));
        messageDao.insertBody(messageItem, new MessageBody(MessageState.PUBLISHED, "Hello  6"));
        MessageItem result = messageDao.getItemById(messageItem.getId());
        Assert.assertEquals(MessageState.UNPUBLISHED, result.getMessageBodyList().get(0).getState());
        Assert.assertEquals("Hello  5", result.getMessageBodyList().get(0).getBody());

        Assert.assertEquals(MessageState.PUBLISHED, result.getMessageBodyList().get(1).getState());
        Assert.assertEquals("Hello  6", result.getMessageBodyList().get(1).getBody());
    }

}
