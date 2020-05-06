package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.request.users.LoginUserRequest;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.RestrictionType;
import net.thumbtack.school.forums.model.types.UserType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static junit.framework.TestCase.fail;

public class TestSessionService extends TestServiceBase {

    @Test
    public void testLogin() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String newCookie = "New_Cookie";
        sessionService.login(newCookie, new LoginUserRequest("My_login", "MypasswordTest"));
        Assert.assertFalse(sessionDao.checkSessionByCookie(cookie));
        Assert.assertTrue(sessionDao.checkSessionByCookie(newCookie));

        sessionService.logout(newCookie);
        Assert.assertFalse(sessionDao.checkSessionByCookie(newCookie));
    }

    @Test
    public void testBadLogin() throws ForumException {
        String cookie = "Test_Cookie";
        userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        String newCookie = "New_Cookie";
        try {
            sessionService.login(newCookie, new LoginUserRequest("My_login", "BadMypasswordTest"));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.LOGIN_OR_PASSWORD_INVALID, fe.getError());
        }
        try {
            sessionService.login(newCookie, new LoginUserRequest("BadMy_login", "MypasswordTest"));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.LOGIN_NOT_FOUND, fe.getError());
        }
    }

    @Test
    public void testDeletedLogin() throws ForumException {
        User deleted = new User("deletedUser", "deletedpassword", "userdeleted", "deletedm@gmail.com", UserType.USER, LocalDate.now(), RestrictionType.FULL, 3);
        userDao.insert(deleted);
        userDao.safeDelete(deleted);

        String newCookie = "New_Cookie";
        try {
            sessionService.login(newCookie, new LoginUserRequest(deleted.getLogin(), deleted.getPassword()));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.LOGIN_NOT_FOUND, fe.getError());
        }

    }
}
