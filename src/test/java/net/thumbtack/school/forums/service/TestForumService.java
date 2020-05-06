package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.request.forums.CreateForumRequest;
import net.thumbtack.school.forums.dto.request.messages.AddCommentRequest;
import net.thumbtack.school.forums.dto.request.messages.ChangeMessageRequest;
import net.thumbtack.school.forums.dto.request.messages.CreateMessageRequest;
import net.thumbtack.school.forums.dto.request.users.LoginUserRequest;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.dto.response.forum.ForumResponse;
import net.thumbtack.school.forums.dto.response.forum.FullInfoForumResponse;
import net.thumbtack.school.forums.dto.response.message.FullInfoMessageResponse;
import net.thumbtack.school.forums.dto.response.message.ListFullInfoMessageResponse;
import net.thumbtack.school.forums.dto.response.message.MessageResponse;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.mockito.ArgumentMatchers.any;

public class TestForumService extends TestServiceBase {

    @Test
    public void testCreateAndDelete() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));
        Assert.assertEquals("MyForumTest", forumResponse.getTopic());
        Assert.assertEquals(ForumType.UNMODERATED, forumResponse.getType());
        Assert.assertNotEquals(0, forumResponse.getId());

        Assert.assertTrue(forumDao.checkForumById(forumResponse.getId()));
        forumService.deleteForum(cookie, forumResponse.getId());
        Assert.assertFalse(forumDao.checkForumById(forumResponse.getId()));

        Mockito.verify(forumDao).delete(any());
        Mockito.verify(forumDao, Mockito.times(0)).safeDelete(any());
    }

    @Test
    public void testBadInsert() throws ForumException, RuntimeException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        try {
            forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.FORUM_DUPLICATE, fe.getError());
        }
    }

    @Test
    public void testDeleteBySuperUser() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        User admin = new User("admin", "adminpassword", "Admin", "admin@gmail.com", UserType.SUPERUSER, LocalDate.now(), RestrictionType.FULL, 999);
        userDao.insert(admin);
        String adminCookie = "AdminCookie";
        sessionService.login(adminCookie, new LoginUserRequest(admin.getLogin(), admin.getPassword()));

        Assert.assertTrue(forumDao.checkForumById(forumResponse.getId()));
        forumService.deleteForum(adminCookie, forumResponse.getId());
        Assert.assertFalse(forumDao.checkForumById(forumResponse.getId()));

        Mockito.verify(forumDao).insert(any());
        Mockito.verify(forumDao).delete(any());
        Mockito.verify(forumDao, Mockito.times(0)).safeDelete(any());
    }

    @Test
    public void testBadDelete() throws ForumException {
        String creator = "Test_Cookie";
        userService.register(creator, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        ForumResponse forumResponse = (ForumResponse) forumService.createForum(creator, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        String notcreator = "Test_Cookie2";
        userService.register(notcreator, new RegisterUserRequest("My_login2", "TestName", "myemail@mail.ru", "MypasswordTest"));
        try {
            forumService.deleteForum(notcreator, forumResponse.getId());
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.NOT_ENOUGH_RIGHTS, fe.getError());
        }
        try {
            forumService.deleteForum("NotMy_Login", forumResponse.getId());
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.USER_UNAUTHORIZED, fe.getError());
        }

        Mockito.verify(forumDao, Mockito.times(0)).delete(any());
        Mockito.verify(forumDao, Mockito.times(0)).safeDelete(any());
    }

    @Test
    public void testGetForum() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.NORMAL, new ArrayList<>()));
        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));
        messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject3", "myBody", MessagePriority.NORMAL, new ArrayList<>()));

        messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody"));

        FullInfoForumResponse fullInfoForumResponse = (FullInfoForumResponse) forumService.getForum(forumResponse.getId(), cookie);

        Assert.assertEquals("My_login", fullInfoForumResponse.getCreator());
        Assert.assertEquals("MyForumTest", fullInfoForumResponse.getTopic());
        Assert.assertEquals(ForumType.UNMODERATED, fullInfoForumResponse.getType());
        Assert.assertEquals(3, fullInfoForumResponse.getCountMessages());
        Assert.assertEquals(1, fullInfoForumResponse.getCountComments());
    }

    @Test
    public void testGetMessagesOnForumWithComments() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.LOW, new ArrayList<>()));
        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));
        messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject3", "myBody", MessagePriority.NORMAL, new ArrayList<>()));

        messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody"));

        ListFullInfoMessageResponse listFullInfoMessageResponse = (ListFullInfoMessageResponse) forumService.getForumMessages(forumResponse.getId(), cookie, false, false, false, Order.ASC, 0, 100);
        List<FullInfoMessageResponse> list = listFullInfoMessageResponse.getList();
        Assert.assertEquals(3, list.size());

        Assert.assertEquals(1, list.get(0).getComments().size());
        Assert.assertEquals(MessagePriority.HIGH, list.get(0).getPriority());

        Assert.assertEquals(0, list.get(1).getComments().size());
        Assert.assertEquals(MessagePriority.NORMAL, list.get(1).getPriority());

        Assert.assertEquals(MessagePriority.LOW, list.get(2).getPriority());
        Assert.assertEquals(0, list.get(2).getComments().size());
    }

    @Test
    public void testGetMessagesOnForumWithCommentsWithLimit() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.LOW, new ArrayList<>()));
        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));

        messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject3", "myBody", MessagePriority.NORMAL, new ArrayList<>()));

        messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody"));
        messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody2"));

        ListFullInfoMessageResponse listFullInfoMessageResponse = (ListFullInfoMessageResponse) forumService.getForumMessages(forumResponse.getId(), cookie, false, false, false, Order.ASC, 0, 1);
        List<FullInfoMessageResponse> list = listFullInfoMessageResponse.getList();
        Assert.assertEquals(1, list.size());

        Assert.assertEquals(2, list.get(0).getComments().size());
        Assert.assertEquals(MessagePriority.HIGH, list.get(0).getPriority());
    }

    @Test
    public void testGetMessagesOnForumWithCommentsWithOffer() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.LOW, new ArrayList<>()));
        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));

        messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject3", "myBody", MessagePriority.NORMAL, new ArrayList<>()));

        messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody"));
        messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody2"));

        ListFullInfoMessageResponse listFullInfoMessageResponse = (ListFullInfoMessageResponse) forumService.getForumMessages(forumResponse.getId(), cookie, false, false, false, Order.ASC, 2, 100);
        List<FullInfoMessageResponse> list = listFullInfoMessageResponse.getList();
        Assert.assertEquals(1, list.size());
        Assert.assertEquals(MessagePriority.LOW, list.get(0).getPriority());
        Assert.assertEquals(0, list.get(0).getComments().size());
    }

    @Test
    public void testGetMessagesOnForumWithCommentsCheckSortTime() throws ForumException, InterruptedException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));
        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));
        Thread.sleep(1000);
        messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody"));
        Thread.sleep(1000);
        messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody2"));
        Thread.sleep(1000);
        messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody3"));
        ListFullInfoMessageResponse listFullInfoMessageResponse = (ListFullInfoMessageResponse) forumService.getForumMessages(forumResponse.getId(), cookie, false, false, false, Order.ASC, 0, 100);

        List<FullInfoMessageResponse> list = listFullInfoMessageResponse.getList();
        Assert.assertEquals(1, list.size());

        Assert.assertEquals(3, list.get(0).getComments().size());
        Assert.assertEquals(MessagePriority.HIGH, list.get(0).getPriority());

        Assert.assertEquals("TestCommentBody", list.get(0).getComments().get(0).getBody().get(0));
        Assert.assertEquals("TestCommentBody2", list.get(0).getComments().get(1).getBody().get(0));
        Assert.assertEquals("TestCommentBody3", list.get(0).getComments().get(2).getBody().get(0));

        ListFullInfoMessageResponse listFullInfoMessageResponseDesc = (ListFullInfoMessageResponse) forumService.getForumMessages(forumResponse.getId(), cookie, false, false, false, Order.DESC, 0, 100);

        List<FullInfoMessageResponse> listDesc = listFullInfoMessageResponseDesc.getList();

        Assert.assertEquals("TestCommentBody", listDesc.get(0).getComments().get(2).getBody().get(0));
        Assert.assertEquals("TestCommentBody2", listDesc.get(0).getComments().get(1).getBody().get(0));
        Assert.assertEquals("TestCommentBody3", listDesc.get(0).getComments().get(0).getBody().get(0));
    }

    @Test
    public void testGetMessagesOnForumNoComments() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.LOW, new ArrayList<>()));
        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));
        messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject3", "myBody", MessagePriority.NORMAL, new ArrayList<>()));

        messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody"));

        ListFullInfoMessageResponse listFullInfoMessageResponse = (ListFullInfoMessageResponse) forumService.getForumMessages(forumResponse.getId(), cookie, false, true, false, Order.ASC, 0, 100);
        List<FullInfoMessageResponse> list = listFullInfoMessageResponse.getList();
        Assert.assertEquals(3, list.size());

        Assert.assertEquals(0, list.get(0).getComments().size());
        Assert.assertEquals(MessagePriority.HIGH, list.get(0).getPriority());

        Assert.assertEquals(0, list.get(1).getComments().size());
        Assert.assertEquals(MessagePriority.NORMAL, list.get(1).getPriority());

        Assert.assertEquals(0, list.get(2).getComments().size());
        Assert.assertEquals(MessagePriority.LOW, list.get(2).getPriority());
    }

    @Test
    public void testGetMessagesOnForumAllVersion() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));

        MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie2, new AddCommentRequest("TestCommentBody"));

        messageService.changeMessage(messageResponse.getId(), cookie, new ChangeMessageRequest("Message new inserted"));
        messageService.changeMessage(messageResponse.getId(), cookie, new ChangeMessageRequest("Message new inserted 2"));

        messageService.changeMessage(commentResponse.getId(), cookie2, new ChangeMessageRequest("Comment new inserted"));

        ListFullInfoMessageResponse listFullInfoMessageResponse = (ListFullInfoMessageResponse) forumService.getForumMessages(forumResponse.getId(), cookie, true, false, false, Order.ASC, 0, 100);
        List<FullInfoMessageResponse> list = listFullInfoMessageResponse.getList();
        Assert.assertEquals(1, list.size());

        Assert.assertEquals(1, list.get(0).getComments().size());

        Assert.assertEquals(3, list.get(0).getBody().size());
        Assert.assertEquals("Message new inserted 2", list.get(0).getBody().get(0));

        Assert.assertEquals(2, list.get(0).getComments().get(0).getBody().size());
        Assert.assertEquals("Comment new inserted", list.get(0).getComments().get(0).getBody().get(0));
    }

    @Test
    public void testGetMessagesOnForumNotAllVersion() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.UNMODERATED));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));

        MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie2, new AddCommentRequest("TestCommentBody"));

        messageService.changeMessage(messageResponse.getId(), cookie, new ChangeMessageRequest("Message new inserted"));
        messageService.changeMessage(messageResponse.getId(), cookie, new ChangeMessageRequest("Message new inserted 2"));

        messageService.changeMessage(commentResponse.getId(), cookie2, new ChangeMessageRequest("Comment new inserted"));

        ListFullInfoMessageResponse listFullInfoMessageResponse = (ListFullInfoMessageResponse) forumService.getForumMessages(forumResponse.getId(), cookie, false, false, false, Order.ASC, 0, 100);
        List<FullInfoMessageResponse> list = listFullInfoMessageResponse.getList();
        Assert.assertEquals(1, list.size());

        Assert.assertEquals(1, list.get(0).getComments().size());

        Assert.assertEquals(1, list.get(0).getBody().size());
        Assert.assertEquals("Message new inserted 2", list.get(0).getBody().get(0));

        Assert.assertEquals(1, list.get(0).getComments().get(0).getBody().size());
        Assert.assertEquals("Comment new inserted", list.get(0).getComments().get(0).getBody().get(0));
    }

    @Test
    public void testGetMessagesOnForumPublished() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.MODERATED));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));

        MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie2, new AddCommentRequest("TestCommentBody"));

        messageService.changeMessage(messageResponse.getId(), cookie, new ChangeMessageRequest("Message new inserted"));
        messageService.changeMessage(messageResponse.getId(), cookie, new ChangeMessageRequest("Message new inserted 2"));

        messageService.changeMessage(commentResponse.getId(), cookie2, new ChangeMessageRequest("Comment new inserted"));

        ListFullInfoMessageResponse listFullInfoMessageResponse = (ListFullInfoMessageResponse) forumService.getForumMessages(forumResponse.getId(), cookie, false, false, true, Order.ASC, 0, 100);
        List<FullInfoMessageResponse> list = listFullInfoMessageResponse.getList();
        Assert.assertEquals(1, list.size());

        Assert.assertEquals(1, list.get(0).getComments().size());

        Assert.assertEquals(1, list.get(0).getBody().size());
        Assert.assertEquals("Message new inserted 2", list.get(0).getBody().get(0));

        Assert.assertEquals(1, list.get(0).getComments().get(0).getBody().size());
        Assert.assertEquals("[" + MessageState.UNPUBLISHED + "]" + "Comment new inserted", list.get(0).getComments().get(0).getBody().get(0));
    }

    @Test
    public void testBadGetMessagesOnForumPublished() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String cookie2 = "Test_Cookie2";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));

        ForumResponse forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.MODERATED));

        MessageResponse messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody", MessagePriority.HIGH, new ArrayList<>()));

        MessageResponse commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie2, new AddCommentRequest("TestCommentBody"));

        messageService.changeMessage(messageResponse.getId(), cookie, new ChangeMessageRequest("Message new inserted"));
        messageService.changeMessage(messageResponse.getId(), cookie, new ChangeMessageRequest("Message new inserted 2"));

        messageService.changeMessage(commentResponse.getId(), cookie2, new ChangeMessageRequest("Comment new inserted"));

        try {
            forumService.getForumMessages(forumResponse.getId(), cookie2, false, false, true, Order.ASC, 0, 100);
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.NOT_ENOUGH_RIGHTS, fe.getError());
        }
    }

}
