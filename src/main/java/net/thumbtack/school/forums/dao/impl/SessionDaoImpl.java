package net.thumbtack.school.forums.dao.impl;

import net.thumbtack.school.forums.dao.SessionDao;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class SessionDaoImpl extends BaseDaoImpl implements SessionDao {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public Session insert(Session session) throws ForumException {
        LOGGER.debug("DAO insert Session {} ", session);
        try {
            sessionMapper.insert(session);
        } catch (RuntimeException ignored) {
            sessionMapper.deleteByUser(session.getUser());
            try {
                sessionMapper.insert(session);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Session {} , {}", session, ex);
                throw new ForumException();
            }
        }
        return session;
    }

    @Override
    public Session getById(int id) throws ForumException {
        LOGGER.debug("DAO get by cookie Session");
        try {
            return sessionMapper.getById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Session {}, {},", id, ex);
            throw new ForumException();
        }
    }

    @Override
    public Session getByLogin(String login) throws ForumException {
        LOGGER.debug("DAO get by User login Session");
        try {
            return sessionMapper.getByUserLogin(login);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Session {}, {},", login, ex);
            throw new ForumException();
        }
    }

    @Override
    public boolean checkSessionByLogin(String login) throws ForumException {
        LOGGER.debug("DAO check Session by User login");
        try {
            return sessionMapper.checkSessionByUserLogin(login);
        } catch (RuntimeException ex) {
            LOGGER.info("Can'tcheck Session {}, {},", login, ex);
            throw new ForumException();
        }
    }

    @Override
    public boolean checkSessionById(int id) throws ForumException {
        LOGGER.debug("DAO check by cookie Session");
        try {
            return sessionMapper.checkSessionById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't check Session {}, {},", id, ex);
            throw new ForumException();
        }
    }

    @Override
    public boolean checkSessionByCookie(String cookie) throws ForumException {
        LOGGER.debug("DAO check by cookie Session");
        try {
            return sessionMapper.checkSessionByCookie(cookie);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't check by cookie Session {},{}", cookie, ex);
            throw new ForumException();
        }
    }

    @Override
    public Session getByCookie(String cookie) throws ForumException {
        LOGGER.debug("DAO get by cookie Session");
        try {
            return sessionMapper.getByCookie(cookie);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get by cookie Session {},{}", cookie, ex);
            throw new ForumException();
        }
    }


    @Override
    public void delete(Session session) throws ForumException {
        LOGGER.debug("DAO delete Session {} ", session);
        try {
            sessionMapper.delete(session);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete Session {} {}", session, ex);
            throw new ForumException();
        }
    }

    @Override
    public void delete(String cookie) throws ForumException {
        LOGGER.debug("DAO delete Session {} ", cookie);
        try {
            sessionMapper.deleteByCookie(cookie);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete Session {} {}", cookie, ex);
            throw new ForumException();
        }
    }
}
