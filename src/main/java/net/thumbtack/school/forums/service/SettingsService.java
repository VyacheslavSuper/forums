package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.dto.response.settings.SettingsResponse;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.User;
import org.springframework.stereotype.Service;

@Service
public class SettingsService extends ServiceBase {

    public ResponseBase getSettings(String cookie) throws ForumException {
        User user = getUserByCookie(cookie);
        return SettingsResponse.toDto(properties, isUserSuper(user));
    }
}
