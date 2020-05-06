package net.thumbtack.school.forums.dao.impl;

import net.thumbtack.school.forums.dao.UserDao;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.ForumType;
import net.thumbtack.school.forums.model.types.RestrictionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public class UserDaoImpl extends BaseDaoImpl implements UserDao {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public User insert(User user) throws ForumException {
        LOGGER.debug("DAO insert User {} ", user);
        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException ex) {
            throw new ForumException(user.getLogin(), ForumErrorCode.LOGIN_ALREADY_EXISTS);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't insert User {} , {}", user, ex);
            throw new ForumException(ForumErrorCode.UNKNOWN_ERROR);
        }
        return user;
    }

    @Override
    public User getById(int id) throws ForumException {
        LOGGER.debug("DAO get User by Id {}", id);
        try {
            return userMapper.getById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get User {},{}", id, ex);
            throw new ForumException();
        }
    }

    @Override
    public User getByLogin(String login) throws ForumException {
        LOGGER.debug("DAO get User by Login {}", login);
        try {
            return userMapper.getByLogin(login);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get User {},{}", login, ex);
            throw new ForumException();
        }
    }

    @Override
    public boolean checkUserById(int id) throws ForumException {
        LOGGER.debug("DAO check User by Id {}", id);
        try {
            return userMapper.checkUserById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't check User by id {},{}", id, ex);
            throw new ForumException();
        }
    }

    @Override
    public boolean checkUserByLogin(String login) throws ForumException {
        LOGGER.debug("DAO check User by Login {}", login);
        try {
            return userMapper.checkUserByLogin(login);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get User {},{}", login, ex);
            throw new ForumException();
        }
    }

    //for test
    @Override
    public List<User> getAll() throws ForumException {
        return getAll(null, null);
    }

    @Override
    public List<User> getAll(Integer limit, Integer offset) throws ForumException {
        LOGGER.debug("DAO get all User ");
        try {
            return userMapper.getAll(limit, offset);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all User", ex);
            throw new ForumException();
        }
    }

    @Override
    public List<User> getUsersByRestriction(RestrictionType restrictionType, Integer limit, Integer offset) throws ForumException {
        LOGGER.debug("DAO get Users by restrictionType ");
        try {
            return userMapper.getUsersByRestrictionType(restrictionType, limit, offset);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get User by restrictionType {},{},{},{}", restrictionType, limit, offset, ex);
            throw new ForumException();
        }
    }

    @Override
    public List<User> getUsersByRestriction(RestrictionType restrictionType) throws ForumException {
        return getUsersByRestriction(restrictionType, null, null);
    }

    @Override
    public User update(User user) throws ForumException {
        LOGGER.debug("DAO get all User ");
        try {
            userMapper.update(user);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all User {} {}", ex, user);
            throw new ForumException();
        }
        return user;
    }

    @Override
    public void unBanUsers(LocalDate time) throws ForumException {
        LOGGER.debug("DAO unBan Users ");
        try {
            userMapper.unBanUsers(time);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't unBan Users {}, {}", time, ex);
            throw new ForumException();
        }
    }

    @Override
    public void safeDelete(User user) throws ForumException {
        LOGGER.debug("DAO safe delete user {} ", user);
        try {
            //now only
            //++delete forums
            user.getForums()
                    .stream()
                    .filter(forum -> forum.getForumType().equals(ForumType.MODERATED))
                    .forEach(forum -> forumMapper.safeDelete(forum));
            userMapper.safeDelete(user);
            user.setDeleted(true);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't safe delete User {} {}", user, ex);
            throw new ForumException();
        }
    }

    @Override
    public void delete(User user) throws ForumException {
        LOGGER.debug("DAO delete user {} ", user);
        try {
            //now cascade delete
            //++delete comments
            //++delete forums
            //++delete messages
            userMapper.delete(user);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete User {} {}", user, ex);
            throw new ForumException(ForumErrorCode.DATABASE_ERROR);
        }
    }

}
