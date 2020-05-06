package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.request.users.LoginUserRequest;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.User;
import org.springframework.stereotype.Service;

@Service
public class SessionService extends ServiceBase {

    public ResponseBase login(String cookie, LoginUserRequest request) throws ForumException {
        User user = checkBeforeLogin(request);
        return login(cookie, user);
    }

    public ResponseBase logout(String cookie) throws ForumException {
        if (!isGetSessionByCookie(cookie)) {
            throw new ForumException(ForumErrorCode.USER_UNAUTHORIZED);
        }
        sessionDao.delete(cookie);
        return new ResponseBase();
    }


}
