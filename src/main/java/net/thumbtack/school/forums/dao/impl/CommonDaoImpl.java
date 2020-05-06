package net.thumbtack.school.forums.dao.impl;

import net.thumbtack.school.forums.dao.CommonDao;
import net.thumbtack.school.forums.exception.ForumErrorCode;
import net.thumbtack.school.forums.exception.ForumException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CommonDaoImpl extends BaseDaoImpl implements CommonDao {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public void clear() throws ForumException {
        LOGGER.debug("Clear Database");
        try {
            debugMapper.deleteUserAll();
            debugMapper.deleteMessageAll();
            debugMapper.deleteForumAll();
            debugMapper.deleteSessionsAll();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't clear database");
            throw new ForumException(ForumErrorCode.WRONG_DEBUG_MAPPER);
        }
    }
}
