package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.request.forums.CreateForumRequest;
import net.thumbtack.school.forums.dto.request.messages.*;
import net.thumbtack.school.forums.dto.request.users.ChangePasswordRequest;
import net.thumbtack.school.forums.dto.request.users.LoginUserRequest;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.dto.response.forum.ForumResponse;
import net.thumbtack.school.forums.dto.response.message.MessageResponse;
import net.thumbtack.school.forums.dto.response.users.UserResponse;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.*;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.fail;

public class TestBannedUser extends TestServiceBase {

    private UserResponse bannedUser;

    private ForumResponse forumResponse;

    private MessageResponse messageResponse;

    private MessageResponse commentResponse;

    private MessageResponse commentResponse2;

    private MessageResponse messageResponseByAdmin;

    private String cookie = "Test_Cookie";

    @BeforeEach
    public void init() throws ForumException {
        User admin = new User("admin", "adminpassword", "Admin", "admin@gmail.com", UserType.SUPERUSER, LocalDate.now(), RestrictionType.FULL, 999);
        userDao.insert(admin);
        String adminCookie = "AdminCookie";
        sessionService.login(adminCookie, new LoginUserRequest(admin.getLogin(), admin.getPassword()));

        bannedUser = (UserResponse) userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        forumResponse = (ForumResponse) forumService.createForum(cookie, new CreateForumRequest("MyForumTest", ForumType.MODERATED));

        messageResponse = (MessageResponse) messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject", "myBody", MessagePriority.HIGH, new ArrayList<>()));

        commentResponse = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody"));

        messageResponseByAdmin = (MessageResponse) messageService.createMessage(forumResponse.getId(), adminCookie, new CreateMessageRequest("exampleSubjectByAdmin", "myBodyAdmin", MessagePriority.HIGH, new ArrayList<>()));

        String cookie2 = "Other_Cookie";
        userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail@mail.ru", "MypasswordTest2"));


        commentResponse2 = (MessageResponse) messageService.createComment(messageResponse.getId(), cookie2, new AddCommentRequest("TestCommentBody"));

        userService.banUser(adminCookie, bannedUser.getId());

    }

    @Test
    public void testMessageServiceAccessRight() throws ForumException {
        try {
            messageService.changeMessage(messageResponse.getId(), cookie, new ChangeMessageRequest("Message new inserted"));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.USER_BANNED, fe.getError());
        }
        try {
            messageService.convertCommentToMessage(commentResponse.getId(), cookie, new CommentIntoMessageRequest("Other", MessagePriority.NORMAL, new ArrayList<>()));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.USER_BANNED, fe.getError());
        }
        try {
            messageService.createComment(messageResponse.getId(), cookie, new AddCommentRequest("TestCommentBody2"));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.USER_BANNED, fe.getError());
        }

        try {
            messageService.createMessage(forumResponse.getId(), cookie, new CreateMessageRequest("exampleSubject2", "myBody2", MessagePriority.HIGH, new ArrayList<>()));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.USER_BANNED, fe.getError());
        }

        messageService.publishMessage(commentResponse2.getId(), cookie, new PublishMessageRequest(Decision.YES));
        messageService.getMessage(messageResponse.getId(), cookie, false, false, false, Order.ASC);
        messageService.addRating(messageResponseByAdmin.getId(), cookie, new AddRatingOnMessageRequest(5));
        messageService.deleteMessageOrComment(commentResponse.getId(), cookie);
    }

    @Test
    public void testForumServiceAccessRight() throws ForumException {
        try {
            forumService.createForum(cookie, new CreateForumRequest("newTest", ForumType.UNMODERATED));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.USER_BANNED, fe.getError());
        }

        forumService.getForumMessages(forumResponse.getId(), cookie, false, false, false, Order.ASC, 0, 100);
        forumService.getForum(forumResponse.getId(), cookie);
        forumService.deleteForum(cookie, forumResponse.getId());
    }

    @Test
    public void testUserServiceAccessRight() throws ForumException {
        userService.changePassword(cookie, new ChangePasswordRequest("My_login", "MypasswordTest", "MypasswordTestEdit"));
        userService.getUsers(cookie);
        sessionService.logout(cookie);
        sessionService.login("newCookie", new LoginUserRequest("My_Login", "MypasswordTestEdit"));
        Assert.assertFalse(sessionDao.checkSessionByCookie(cookie));
    }

}
