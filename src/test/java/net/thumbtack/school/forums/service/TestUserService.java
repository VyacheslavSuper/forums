package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.request.users.ChangePasswordRequest;
import net.thumbtack.school.forums.dto.request.users.LoginUserRequest;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.dto.response.users.FullUserResponse;
import net.thumbtack.school.forums.dto.response.users.ListFullUserResponse;
import net.thumbtack.school.forums.dto.response.users.UserResponse;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.RestrictionType;
import net.thumbtack.school.forums.model.types.UserType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

public class TestUserService extends TestServiceBase {

    @Test
    public void testRegistration() throws ForumException, RuntimeException {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest");
        String cookie = "Test_Cookie";

        UserResponse response = (UserResponse) userService.register(cookie, registerUserRequest);

        Assert.assertEquals(registerUserRequest.getEmail(), response.getEmail());
        Assert.assertEquals(registerUserRequest.getLogin(), response.getLogin());

        try {
            userService.register(cookie, registerUserRequest);
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.LOGIN_ALREADY_EXISTS, fe.getError());
        }

        Mockito.verify(userDao, Mockito.times(2)).insert(any());
        Mockito.verify(sessionDao).insert(any());
    }

    @Test
    public void testChangePass() throws ForumException {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest");
        String cookie = "Test_Cookie";

        UserResponse response = (UserResponse) userService.register(cookie, registerUserRequest);

        String newpassword = "newpasswordTest";
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(registerUserRequest.getLogin(), registerUserRequest.getPassword(), newpassword);

        UserResponse changeResponse = (UserResponse) userService.changePassword(cookie, changePasswordRequest);

        Assert.assertEquals(registerUserRequest.getEmail(), changeResponse.getEmail());
        Assert.assertEquals(registerUserRequest.getLogin(), changeResponse.getLogin());

        Assert.assertEquals(newpassword, userDao.getById(response.getId()).getPassword());

        Mockito.verify(userDao).insert(any());
        Mockito.verify(userDao).update(any());
        Mockito.verify(sessionDao).insert(any());
    }

    @Test
    public void testBadChangePass() throws ForumException {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest");
        String cookie = "Test_Cookie";
        UserResponse response = (UserResponse) userService.register(cookie, registerUserRequest);
        String newpassword = "newpasswordTest";

        try {
            userService.changePassword(cookie, new ChangePasswordRequest("notMyLogin", registerUserRequest.getPassword(), newpassword));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.LOGIN_OR_PASSWORD_INVALID, fe.getError());
        }
        try {
            userService.changePassword(cookie, new ChangePasswordRequest(registerUserRequest.getLogin(), "notMyPassword", newpassword));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.LOGIN_OR_PASSWORD_INVALID, fe.getError());
        }
        try {
            userService.changePassword("bad_cookie", new ChangePasswordRequest(registerUserRequest.getLogin(), registerUserRequest.getPassword(), newpassword));
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.USER_UNAUTHORIZED, fe.getError());
        }
        Mockito.verify(userDao, Mockito.times(0)).update(any());
    }

    @Test
    public void testDeleteUser() throws ForumException {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest");
        String cookie = "Test_Cookie";

        UserResponse response = (UserResponse) userService.register(cookie, registerUserRequest);

        userService.deleteUser(cookie);

        Mockito.verify(userDao).delete(any());
        Mockito.verify(userDao).insert(any());
        Mockito.verify(sessionDao).insert(any());
    }

    @Test
    public void testBadDeleteUser() throws ForumException {
        try {
            userService.deleteUser("badCookie");
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.USER_UNAUTHORIZED, fe.getError());
        }
        Mockito.verify(userDao, Mockito.times(0)).delete(any());
    }

    @Test
    public void testSetAdmin() throws ForumException {
        User admin = new User("admin", "adminpassword", "Admin", "admin@gmail.com", UserType.SUPERUSER, LocalDate.now(), RestrictionType.FULL, 999);
        userDao.insert(admin);
        String adminCookie = "AdminCookie";
        sessionService.login(adminCookie, new LoginUserRequest(admin.getLogin(), admin.getPassword()));


        String cookie = "Test_Cookie";
        UserResponse response = (UserResponse) userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        userService.setSuperUser(adminCookie, response.getId());
        User user = userDao.getById(response.getId());
        Assert.assertEquals(UserType.SUPERUSER, user.getUserType());

        try {
            userService.setSuperUser(adminCookie, response.getId());
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.ALREADY_SUPERUSER, fe.getError());
        }

        Mockito.verify(userDao).update(any());
    }

    @Test
    public void testBadSetAdmin() throws ForumException {
        User admin = new User("admin", "adminpassword", "Admin", "admin@gmail.com", UserType.SUPERUSER, LocalDate.now(), RestrictionType.FULL, 999);
        userDao.insert(admin);
        String adminCookie = "AdminCookie";
        sessionService.login(adminCookie, new LoginUserRequest(admin.getLogin(), admin.getPassword()));

        String cookie = "Test_Cookie";
        UserResponse response = (UserResponse) userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));


        try {
            userService.setSuperUser(adminCookie, admin.getId());
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.ALREADY_SUPERUSER, fe.getError());
        }
        try {
            userService.setSuperUser("notAdminCookie", response.getId());
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.USER_UNAUTHORIZED, fe.getError());
        }
        try {
            userService.setSuperUser(cookie, response.getId());
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.NOT_ENOUGH_RIGHTS, fe.getError());
        }
        Mockito.verify(userDao, Mockito.times(0)).update(any());
    }

    @Test
    public void testGetUsersByUser() throws ForumException {
        String cookie = "Test_Cookie1";
        UserResponse response = (UserResponse) userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        String cookie2 = "Test_Cookie2";
        UserResponse response2 = (UserResponse) userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));
        String cookie3 = "Test_Cookie3";
        UserResponse response3 = (UserResponse) userService.register(cookie3, new RegisterUserRequest("My_login3", "TestName3", "myemail3@mail.ru", "MypasswordTest3"));

        //Выход второго пользователя
        sessionService.logout(cookie2);

        ListFullUserResponse listFullUserResponse = (ListFullUserResponse) userService.getUsers(cookie);
        List<FullUserResponse> listuserslist = listFullUserResponse.getList();

        Assert.assertEquals("My_login", listuserslist.get(0).getName());
        Assert.assertTrue(listuserslist.get(0).isOnline());
        Assert.assertFalse(listuserslist.get(0).isDeleted());
        Assert.assertNull(listuserslist.get(0).getEmail());
        Assert.assertNull(listuserslist.get(0).getSuperuser());

        Assert.assertEquals("My_login2", listuserslist.get(1).getName());
        Assert.assertFalse(listuserslist.get(1).isOnline());
        Assert.assertFalse(listuserslist.get(1).isDeleted());
        Assert.assertNull(listuserslist.get(1).getEmail());
        Assert.assertNull(listuserslist.get(1).getSuperuser());

        Assert.assertEquals("My_login3", listuserslist.get(2).getName());
        Assert.assertTrue(listuserslist.get(2).isOnline());
        Assert.assertFalse(listuserslist.get(2).isDeleted());
        Assert.assertNull(listuserslist.get(2).getEmail());
        Assert.assertNull(listuserslist.get(2).getSuperuser());

        Mockito.verify(responseDao).getListUserView(anyBoolean());
    }

    @Test
    public void testGetUsersBySuperUser() throws ForumException {
        String cookie = "Test_Cookie1";
        UserResponse response = (UserResponse) userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));
        String cookie2 = "Test_Cookie2";
        UserResponse response2 = (UserResponse) userService.register(cookie2, new RegisterUserRequest("My_login2", "TestName2", "myemail2@mail.ru", "MypasswordTest2"));
        String cookie3 = "Test_Cookie3";
        UserResponse response3 = (UserResponse) userService.register(cookie3, new RegisterUserRequest("My_login3", "TestName3", "myemail3@mail.ru", "MypasswordTest3"));

        //Выход второго пользователя
        sessionService.logout(cookie2);


        User admin = new User("admin", "adminpassword", "Admin", "admin@gmail.com", UserType.SUPERUSER, LocalDate.now(), RestrictionType.FULL, 999);
        userDao.insert(admin);
        String adminCookie = "AdminCookie";
        sessionService.login(adminCookie, new LoginUserRequest(admin.getLogin(), admin.getPassword()));


        ListFullUserResponse listFullUserResponse = (ListFullUserResponse) userService.getUsers(adminCookie);
        List<FullUserResponse> listuserslist = listFullUserResponse.getList();

        Assert.assertEquals("My_login", listuserslist.get(0).getName());
        Assert.assertTrue(listuserslist.get(0).isOnline());
        Assert.assertFalse(listuserslist.get(0).isDeleted());
        Assert.assertNotNull(listuserslist.get(0).getEmail());
        Assert.assertNotNull(listuserslist.get(0).getSuperuser());

        Assert.assertEquals("My_login2", listuserslist.get(1).getName());
        Assert.assertFalse(listuserslist.get(1).isOnline());
        Assert.assertFalse(listuserslist.get(1).isDeleted());
        Assert.assertNotNull(listuserslist.get(1).getEmail());
        Assert.assertNotNull(listuserslist.get(1).getSuperuser());

        Assert.assertEquals("My_login3", listuserslist.get(2).getName());
        Assert.assertTrue(listuserslist.get(2).isOnline());
        Assert.assertFalse(listuserslist.get(2).isDeleted());
        Assert.assertNotNull(listuserslist.get(2).getEmail());
        Assert.assertNotNull(listuserslist.get(2).getSuperuser());

        Assert.assertEquals("admin", listuserslist.get(3).getName());
        Assert.assertTrue(listuserslist.get(3).isOnline());
        Assert.assertTrue(listuserslist.get(3).getSuperuser());

        Mockito.verify(responseDao).getListUserView(anyBoolean());
    }

    @Test
    public void testBanUser() throws ForumException {
        User admin = new User("admin", "adminpassword", "Admin", "admin@gmail.com", UserType.SUPERUSER, LocalDate.now(), RestrictionType.FULL, 999);
        userDao.insert(admin);
        String adminCookie = "AdminCookie";
        sessionService.login(adminCookie, new LoginUserRequest(admin.getLogin(), admin.getPassword()));


        String cookie = "Test_Cookie";
        UserResponse response = (UserResponse) userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        userService.banUser(adminCookie, response.getId());
        Assert.assertEquals(RestrictionType.LIM, userDao.getById(response.getId()).getRestrictionType());

        for (int i = 0; i < properties.getBanCount(); i++) {
            userService.banUser(adminCookie, response.getId());
        }
        Assert.assertEquals(RestrictionType.MAXLIM, userDao.getById(response.getId()).getRestrictionType());
    }

    @Test
    public void testBadBanUser() throws ForumException {
        User admin = new User("admin", "adminpassword", "Admin", "admin@gmail.com", UserType.SUPERUSER, LocalDate.now(), RestrictionType.FULL, 999);
        userDao.insert(admin);
        String adminCookie = "AdminCookie";
        sessionService.login(adminCookie, new LoginUserRequest(admin.getLogin(), admin.getPassword()));

        String cookie = "Test_Cookie";
        UserResponse response = (UserResponse) userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        try {
            userService.banUser(adminCookie, admin.getId());
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.ALREADY_SUPERUSER, fe.getError());
        }

        try {
            userService.banUser("notAdminCookie", response.getId());
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.USER_UNAUTHORIZED, fe.getError());
        }

        try {
            userService.banUser(cookie, response.getId());
            fail();
        } catch (ForumException fe) {
            Assert.assertEquals(ForumErrorCode.NOT_ENOUGH_RIGHTS, fe.getError());
        }

        Mockito.verify(userDao, Mockito.times(0)).update(any());
    }

}

