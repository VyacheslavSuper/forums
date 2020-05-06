package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.request.forums.CreateForumRequest;
import net.thumbtack.school.forums.dto.request.messages.*;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.dto.response.forum.ForumResponse;
import net.thumbtack.school.forums.dto.response.message.FullInfoCommentResponse;
import net.thumbtack.school.forums.dto.response.message.FullInfoMessageResponse;
import net.thumbtack.school.forums.dto.response.message.MessageResponse;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.types.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;

public class TestMessageService extends TestServiceBase {
    @Test
    public void testInsert() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody"));

        MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody"));

        Assert.assertEquals(MessagePriority.NORMAL, messageDao.getItemById(messageResponse.getId()).getMessageHeader().getPriority());
        Assert.assertEquals(0, messageDao.getItemById(messageResponse.getId()).getMessageHeader().getMessageTags().size());

        Assert.assertEquals(messageDao.getItemById(messageResponse.getId()), messageDao.getItemById(commentResponse.getId()).getParent());
        Assert.assertEquals(1, messageDao.getItemById(messageResponse.getId()).getCountComments());
        Assert.assertEquals(1, messageDao.getItemById(messageResponse.getId()).getMessages().size());
    }

    @Test
    public void testBadInsertComment() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.MODERATED));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject2", "myBody"));

        try {
            messageService.createComment(messageResponse.getId(), cookie2, new AddCommentRequest("TestCommentBody"));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.MESSAGE_NOT_FOUND, fe.getError());
        }

        Forum forum = forumDao.getById(forumResponse.getId());
        forumDao.safeDelete(forum);
        try {
            messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject3", "myBody3"));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.FORUM_IS_READONLY, fe.getError());
        }
        try {
            messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody"));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.FORUM_IS_READONLY, fe.getError());
        }
    }

    @Test
    public void testDeleteMessage() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject2", "myBody"));

        MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie2, new AddCommentRequest("TestCommentBody"));

        Assert.assertTrue(messageDao.checkMessageItemById(commentResponse.getId()));
        messageService.deleteMessageOrComment(commentResponse.getId(), cookie2);
        Assert.assertFalse(messageDao.checkMessageItemById(commentResponse.getId()));

        Assert.assertTrue(messageDao.checkMessageItemById(messageResponse.getId()));
        messageService.deleteMessageOrComment(messageResponse.getId(), cookie2);
        Assert.assertFalse(messageDao.checkMessageItemById(messageResponse.getId()));

        Assert.assertEquals(0, forumDao.getById(forumResponse.getId()).getCountMessages());
        Assert.assertEquals(0, forumDao.getById(forumResponse.getId()).getCountComments());
    }

    @Test
    public void testBadDeleteMessage() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        String cookie3 = "Test_Cookie3";
        userService.register(cookie3, new RegisterUserRequest("My_login3", "TestName3", "myemail3@mail.ru", "MypasswordTest3"));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject2", "myBody"));

        MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie2, new AddCommentRequest("TestCommentBody"));

        try {
            messageService.deleteMessageOrComment(messageResponse.getId(), cookie2);
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.DELETE_MESSAGE_IMPOSSIBLE, fe.getError());
        }

        try {
            messageService.deleteMessageOrComment(commentResponse.getId(), cookie3);
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.NOT_ENOUGH_RIGHTS, fe.getError());
        }
    }

    @Test
    public void testChangeMessageToModerated() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.MODERATED));

        MessageResponse messageResponseByAuthor = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));

        MessageResponse commentResponseUnpublish = (MessageResponse) messageService.createComment(messageResponseByAuthor.getId(), cookie2, new AddCommentRequest("TestCommentBody"));

        messageService.changeMessage(messageResponseByAuthor.getId(), cookie, new ChangeMessageRequest("Message new inserted"));
        messageService.changeMessage(messageResponseByAuthor.getId(), cookie, new ChangeMessageRequest("Message new inserted 2"));

        messageService.changeMessage(commentResponseUnpublish.getId(), cookie2, new ChangeMessageRequest("CommentUnPublish updated"));

        MessageResponse commentResponsePublish = (MessageResponse) messageService.createComment(messageResponseByAuthor.getId(), cookie2, new AddCommentRequest("TestCommentBody2"));
        messageService.publishMessage(commentResponsePublish.getId(), cookie, new PublishMessageRequest(Decision.YES));

        messageService.changeMessage(commentResponsePublish.getId(), cookie2, new ChangeMessageRequest("Comment UnPublish inserted"));

        Assert.assertEquals(3, messageDao.getItemById(messageResponseByAuthor.getId()).getMessageBodyList().size());
        Assert.assertEquals("Message new inserted 2", messageDao.getItemById(messageResponseByAuthor.getId()).getMessageBodyList().get(0).getBody());
        Assert.assertEquals(MessageState.PUBLISHED, messageDao.getItemById(messageResponseByAuthor.getId()).getMessageBodyList().get(0).getState());

        Assert.assertEquals(1, messageDao.getItemById(commentResponseUnpublish.getId()).getMessageBodyList().size());
        Assert.assertEquals("CommentUnPublish updated", messageDao.getItemById(commentResponseUnpublish.getId()).getMessageBodyList().get(0).getBody());
        Assert.assertEquals(MessageState.UNPUBLISHED, messageDao.getItemById(commentResponseUnpublish.getId()).getMessageBodyList().get(0).getState());


        Assert.assertEquals(2, messageDao.getItemById(commentResponsePublish.getId()).getMessageBodyList().size());
        Assert.assertEquals("Comment UnPublish inserted", messageDao.getItemById(commentResponsePublish.getId()).getMessageBodyList().get(0).getBody());
        Assert.assertEquals(MessageState.UNPUBLISHED, messageDao.getItemById(commentResponsePublish.getId()).getMessageBodyList().get(0).getState());

        Assert.assertEquals("TestCommentBody2", messageDao.getItemById(commentResponsePublish.getId()).getMessageBodyList().get(1).getBody());
        Assert.assertEquals(MessageState.PUBLISHED, messageDao.getItemById(commentResponsePublish.getId()).getMessageBodyList().get(1).getState());
    }

    @Test
    public void testChangeMessageToUnModerated() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        MessageResponse messageResponseByAuthor = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));

        MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponseByAuthor.getId(), cookie2, new AddCommentRequest("TestCommentBody"));

        messageService.changeMessage(messageResponseByAuthor.getId(), cookie, new ChangeMessageRequest("Message new inserted"));
        messageService.changeMessage(messageResponseByAuthor.getId(), cookie, new ChangeMessageRequest("Message new inserted 2"));

        messageService.changeMessage(commentResponse.getId(), cookie2, new ChangeMessageRequest("CommentBody inserted"));

        Assert.assertEquals(3, messageDao.getItemById(messageResponseByAuthor.getId()).getMessageBodyList().size());
        Assert.assertEquals("Message new inserted 2", messageDao.getItemById(messageResponseByAuthor.getId()).getMessageBodyList().get(0).getBody());
        Assert.assertEquals(MessageState.PUBLISHED, messageDao.getItemById(messageResponseByAuthor.getId()).getMessageBodyList().get(0).getState());

        Assert.assertEquals(2, messageDao.getItemById(commentResponse.getId()).getMessageBodyList().size());
        Assert.assertEquals("CommentBody inserted", messageDao.getItemById(commentResponse.getId()).getMessageBodyList().get(0).getBody());
        Assert.assertEquals(MessageState.PUBLISHED, messageDao.getItemById(commentResponse.getId()).getMessageBodyList().get(0).getState());
    }

    @Test
    public void testBadChangeMessage() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        MessageResponse messageResponseByAuthor = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));

        try {
            messageService.changeMessage(messageResponseByAuthor.getId(), cookie2, new ChangeMessageRequest("bad message"));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.NOT_ENOUGH_RIGHTS, fe.getError());
        }
    }

    @Test
    public void testChangePriority() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.LOW, new ArrayList<>()));

        messageService.changePriority(messageResponse.getId(), cookie2, new ChangePriorityRequest(MessagePriority.HIGH));

        Assert.assertEquals(MessagePriority.HIGH, messageDao.getItemById(messageResponse.getId()).getMessageHeader().getPriority());

        Mockito.verify(messageDao).updateHeader(any());
        Mockito.verify(messageDao, Mockito.times(0)).updateItem(any());
    }

    @Test
    public void testBadChangePriority() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.LOW, new ArrayList<>()));

        try {
            messageService.changePriority(messageResponse.getId(), cookie2, new ChangePriorityRequest(MessagePriority.LOW));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.PRIORITY_MATCHES, fe.getError());
        }
        try {
            messageService.changePriority(messageResponse.getId(), cookie, new ChangePriorityRequest(MessagePriority.HIGH));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.NOT_ENOUGH_RIGHTS, fe.getError());
        }
    }

    @Test
    public void testPublish() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.MODERATED));

        MessageResponse messageResponseYes = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.LOW, new ArrayList<>()));

        MessageResponse messageResponseNo = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject2", "myBody2", MessagePriority.LOW, new ArrayList<>()));
        messageService.publishMessage(messageResponseYes.getId(), cookie, new PublishMessageRequest(Decision.YES));
        messageService.publishMessage(messageResponseNo.getId(), cookie, new PublishMessageRequest(Decision.NO));

        Assert.assertTrue(messageDao.checkMessageItemById(messageResponseYes.getId()));
        Assert.assertFalse(messageDao.checkMessageItemById(messageResponseNo.getId()));

        Assert.assertEquals(MessageState.PUBLISHED, messageDao.getItemById(messageResponseYes.getId()).getMessageBodyList().get(0).getState());
    }

    @Test
    public void testBadPublishInUnmoderated() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        MessageResponse messageResponseYes = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.LOW, new ArrayList<>()));
        try {
            messageService.publishMessage(messageResponseYes.getId(), cookie, new PublishMessageRequest(Decision.YES));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.FORUM_UNMODERATED, fe.getError());
        }
    }

    @Test
    public void testBadPublish() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        String cookie3 = "Test_Cookie3";
        userService.register(cookie3, new RegisterUserRequest("My_login3", "TestName3", "myemail3@mail.ru", "MypasswordTest3"));


        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.MODERATED));

        MessageResponse messageResponseYes = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.LOW, new ArrayList<>()));
        try {
            messageService.publishMessage(messageResponseYes.getId(), cookie3, new PublishMessageRequest(Decision.YES));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.NOT_ENOUGH_RIGHTS, fe.getError());
        }

        messageService.publishMessage(messageResponseYes.getId(), cookie, new PublishMessageRequest(Decision.YES));
        try {
            messageService.publishMessage(messageResponseYes.getId(), cookie, new PublishMessageRequest(Decision.YES));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.MESSAGEBODY_FULL_PUBLISHED, fe.getError());
        }
    }

    @Test
    public void testAddRating() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        String cookie3 = "Test_Cookie3";
        userService.register(cookie3, new RegisterUserRequest("My_login3", "TestName3", "myemail3@mail.ru", "MypasswordTest3"));


        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.MODERATED));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.LOW, new ArrayList<>()));

        messageService.addRating(messageResponse.getId(), cookie3, new AddRatingOnMessageRequest(5));
        Assert.assertEquals(1, messageDao.getItemById(messageResponse.getId()).getMessageRatingList().size());
        Assert.assertEquals(5, messageDao.getItemById(messageResponse.getId()).getMessageRatingList().get(0).getRating());
        Assert.assertEquals(1, messageDao.getItemById(messageResponse.getId()).getRated());

        messageService.addRating(messageResponse.getId(), cookie3, new AddRatingOnMessageRequest(4));
        Assert.assertEquals(1, messageDao.getItemById(messageResponse.getId()).getMessageRatingList().size());
        Assert.assertEquals(4, messageDao.getItemById(messageResponse.getId()).getMessageRatingList().get(0).getRating());
        Assert.assertEquals(1, messageDao.getItemById(messageResponse.getId()).getRated());

        messageService.addRating(messageResponse.getId(), cookie3, new AddRatingOnMessageRequest(null));
        Assert.assertEquals(0, messageDao.getItemById(messageResponse.getId()).getMessageRatingList().size());
        Assert.assertEquals(0, messageDao.getItemById(messageResponse.getId()).getRated());

        Mockito.verify(messageDao, Mockito.times(2)).addRatingOnMessage(any(), any());
        Mockito.verify(messageDao).delRatingOnMessage(any(), any());
    }

    @Test
    public void testBadAddRating() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));


        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.MODERATED));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.LOW, new ArrayList<>()));

        try {
            messageService.addRating(messageResponse.getId(), cookie2, new AddRatingOnMessageRequest(5));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.USER_MATCHES, fe.getError());
        }
    }

    @Test
    public void testConvertComment() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject2", "myBody"));

        MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie2, new AddCommentRequest("TestCommentBody"));

        messageService.convertCommentToMessage(commentResponse.getId(), cookie, new CommentIntoMessageRequest("Other", MessagePriority.NORMAL, new ArrayList<>()));
        Assert.assertNull(messageDao.getItemById(commentResponse.getId()).getParent());
        Assert.assertNotEquals(messageDao.getItemById(messageResponse.getId()).getMessageHeader(), messageDao.getItemById(commentResponse.getId()).getMessageHeader());

        Mockito.verify(messageDao).convertCommentToMessageAndUpdate(any());
    }

    @Test
    public void testBadConvertComment() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject2", "myBody"));

        MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie2, new AddCommentRequest("TestCommentBody"));

        try {
            messageService.convertCommentToMessage(commentResponse.getId(), cookie2, new CommentIntoMessageRequest("Other", MessagePriority.NORMAL, new ArrayList<>()));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.NOT_ENOUGH_RIGHTS, fe.getError());
        }

        try {
            messageService.convertCommentToMessage(messageResponse.getId(), cookie, new CommentIntoMessageRequest("Other", MessagePriority.NORMAL, new ArrayList<>()));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.CONVERSION_COMMENT_IMPOSSIBLE, fe.getError());
        }
    }

    @Test
    public void testGetMessage() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject2", "myBody"));

        MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody"));

        FullInfoMessageResponse fullInfoMessageResponse = (FullInfoMessageResponse) messageService.getMessage(messageResponse.getId(), cookie2, false, false, false, Order.ASC);

        Assert.assertEquals("exampleSubject2", fullInfoMessageResponse.getTopic());
        Assert.assertEquals(MessagePriority.NORMAL, fullInfoMessageResponse.getPriority());
        Assert.assertEquals("My_login2", fullInfoMessageResponse.getCreator());
        Assert.assertEquals(1, fullInfoMessageResponse.getBody().size());
        Assert.assertEquals(1, fullInfoMessageResponse.getComments().size());

        FullInfoCommentResponse fullInfoCommentResponse = (FullInfoCommentResponse) messageService.getMessage(commentResponse.getId(), cookie2, false, false, false, Order.ASC);
        Assert.assertEquals(fullInfoCommentResponse, fullInfoMessageResponse.getComments().get(0));

        Assert.assertEquals("My_login", fullInfoCommentResponse.getCreator());
        Assert.assertEquals(1, fullInfoCommentResponse.getBody().size());
        Assert.assertEquals(0, fullInfoCommentResponse.getComments().size());
    }

    @Test
    public void testBadGetMessage() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie2, new CreateMessageRequest("exampleSubject2", "myBody"));

        try {
            messageService.getMessage(messageResponse.getId(), cookie2, false, false, true, Order.ASC);
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.NOT_ENOUGH_RIGHTS, fe.getError());
        }
    }

}
