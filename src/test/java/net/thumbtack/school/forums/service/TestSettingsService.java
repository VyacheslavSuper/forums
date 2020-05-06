package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.request.users.LoginUserRequest;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.dto.response.settings.SettingsResponse;
import net.thumbtack.school.forums.dto.response.users.UserResponse;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.RestrictionType;
import net.thumbtack.school.forums.model.types.UserType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class TestSettingsService extends TestServiceBase {

    @Test
    public void getSettingsByUser() throws ForumException {
        String cookie = "Test_Cookie";
        UserResponse response = (UserResponse) userService.register(cookie, new RegisterUserRequest("My_login", "TestName", "myemail@mail.ru", "MypasswordTest"));

        SettingsResponse settingsResponse = (SettingsResponse) settingsService.getSettings(cookie);
        Assert.assertNull(settingsResponse.getBanTime());
        Assert.assertNull(settingsResponse.getMaxBanCount());
        Assert.assertNotNull(settingsResponse.getMaxNameLength());
        Assert.assertNotNull(settingsResponse.getMinPasswordLength());

        Assert.assertEquals(properties.getMaxNameLengthString(), settingsResponse.getMaxNameLength());
        Assert.assertEquals(properties.getMinPasswordLengthString(), settingsResponse.getMinPasswordLength());

    }

    @Test
    public void getSettingsBySuperUser() throws ForumException {
        User admin = new User("admin", "adminpassword", "Admin", "admin@gmail.com", UserType.SUPERUSER, LocalDate.now(), RestrictionType.FULL, 999);
        userDao.insert(admin);
        String adminCookie = "AdminCookie";
        sessionService.login(adminCookie, new LoginUserRequest(admin.getLogin(), admin.getPassword()));

        SettingsResponse settingsResponse = (SettingsResponse) settingsService.getSettings(adminCookie);
        Assert.assertNotNull(settingsResponse.getBanTime());
        Assert.assertNotNull(settingsResponse.getMaxBanCount());
        Assert.assertNotNull(settingsResponse.getMaxNameLength());
        Assert.assertNotNull(settingsResponse.getMinPasswordLength());

        Assert.assertEquals(properties.getBanTimeString(), settingsResponse.getBanTime());
        Assert.assertEquals(properties.getBanCountString(), settingsResponse.getMaxBanCount());
        Assert.assertEquals(properties.getMaxNameLengthString(), settingsResponse.getMaxNameLength());
        Assert.assertEquals(properties.getMinPasswordLengthString(), settingsResponse.getMinPasswordLength());

    }
}
