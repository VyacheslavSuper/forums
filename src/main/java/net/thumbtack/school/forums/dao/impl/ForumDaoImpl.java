package net.thumbtack.school.forums.dao.impl;

import net.thumbtack.school.forums.dao.ForumDao;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ForumDaoImpl extends BaseDaoImpl implements ForumDao {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public Forum insert(Forum forum) throws ForumException {
        LOGGER.debug("DAO insert Forum {} ", forum);
        try {
            forumMapper.insert(forum);
        } catch (DuplicateKeyException dke) {
            throw new ForumException(ForumErrorCode.FORUM_DUPLICATE);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't insert Forum {} , {}", forum, ex);
            throw new ForumException();
        }
        return forum;
    }

    @Override
    public Forum getById(int id) throws ForumException {
        LOGGER.debug("DAO get Forum by Id {}", id);
        try {
            return forumMapper.getById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get Forum by id {} , {}", id, ex);
            throw new ForumException();
        }
    }

    @Override
    public boolean checkForumById(int id) throws ForumException {
        LOGGER.debug("DAO check Forum by Id {}", id);
        try {
            return forumMapper.checkForumById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't check Forum by id {} , {}", id, ex);
            throw new ForumException();
        }
    }

    @Override
    public List<Forum> getAll() throws ForumException {
        LOGGER.debug("DAO get all Forums");
        try {
            return forumMapper.getAll();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all Forum", ex);
            throw new ForumException();
        }
    }

    @Override
    public List<Forum> getAll(Integer limit, Integer offset) throws ForumException {
        LOGGER.debug("DAO get all Forums");
        try {
            return forumMapper.getAllWithParams(limit, offset);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all Forum", ex);
            throw new ForumException();
        }
    }

    @Override
    public Forum update(Forum forum) throws ForumException {
        LOGGER.debug("DAO update Forum {}", forum);
        try {
            forumMapper.update(forum);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't update Forum {} {}", ex, forum);
            throw new ForumException();
        }
        return forum;
    }

    @Override
    public void safeDelete(Forum forum) throws ForumException {
        LOGGER.debug("DAO safe delete Forum {}", forum);
        try {
            forumMapper.safeDelete(forum);
            forum.setReadonly(true);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't safe delete Forum {} {}", ex, forum);
            throw new ForumException();
        }
    }

    @Override
    public void delete(Forum forum) throws ForumException {
        LOGGER.debug("DAO delete Forum {}", forum);
        try {
            forumMapper.delete(forum);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't delete Forum {} {}", ex, forum);
            throw new ForumException();
        }
    }
}
