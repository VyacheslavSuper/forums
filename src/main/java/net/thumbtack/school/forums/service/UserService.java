package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dto.request.users.ChangePasswordRequest;
import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.dto.response.users.FullUserResponse;
import net.thumbtack.school.forums.dto.response.users.ListFullUserResponse;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.RestrictionType;
import net.thumbtack.school.forums.model.types.UserType;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService extends ServiceBase {

    public ResponseBase register(String cookie, RegisterUserRequest request) throws ForumException {
        User user = userDao.insert(userMapStruct.userFromRegisterRequest(request));
        return login(cookie, user);
    }

    public ResponseBase changePassword(String cookie, ChangePasswordRequest request) throws ForumException {
        User user = getUserByCookie(cookie);
        if (!user.getLogin().equalsIgnoreCase(request.getLogin())
                || !user.getPassword().equals(request.getOldPassword())) {
            throw new ForumException(ForumErrorCode.LOGIN_OR_PASSWORD_INVALID);
        }
        user.setPassword(request.getPassword());
        userDao.update(user);
        return userMapStruct.userToUserResponse(user);
    }

    public ResponseBase deleteUser(String cookie) throws ForumException {
        User user = getUserByCookie(cookie);
        if (!user.getForums().isEmpty()
                || !user.getMessages().isEmpty()) {
            userDao.safeDelete(user);
        } else {
            userDao.delete(user);
        }
        return new ResponseBase();
    }

    public ResponseBase setSuperUser(String cookie, int id) throws ForumException {
        User superUser = getUserByCookie(cookie);

        checkEnoughRightsUser(superUser);

        User newSuperUser = getUserById(id);

        checkAlreadySuperUser(newSuperUser);

        newSuperUser.setUserType(UserType.SUPERUSER);
        newSuperUser.setRestrictionType(RestrictionType.FULL);
        userDao.update(newSuperUser);
        return new ResponseBase();
    }

    public ResponseBase getUsers(String cookie) throws ForumException {
        User user = getUserByCookie(cookie);
        List<FullUserResponse> list = new ArrayList<>();
        responseDao.getListUserView(isUserSuper(user))
                .forEach(userView -> list.add(userMapStruct.userViewToFullUserResponse(userView)));
        return new ListFullUserResponse(list);
    }

    public ResponseBase banUser(String cookie, int id) throws ForumException {
        User superUser = getUserByCookie(cookie);

        checkEnoughRightsUser(superUser);

        User restrictionUser = getUserById(id);

        checkAlreadySuperUser(restrictionUser);

        if (restrictionUser.getBanCount() < properties.getBanCount()) {
            setUserRestriction(restrictionUser, RestrictionType.LIM, restrictionUser.getBanCount() + 1, LocalDate.now().plusDays(properties.getBanTime()));
        } else {
            setUserRestriction(restrictionUser, RestrictionType.MAXLIM, properties.getBanCount(), LocalDate.parse("9999-01-01"));
        }
        return new ResponseBase();
    }

    private void setUserRestriction(User user, RestrictionType type, int banCount, LocalDate banTime) throws ForumException {
        user.setRestrictionType(type);
        user.setBanCount(banCount);
        user.setBanTime(banTime);
        userDao.update(user);
    }
}

