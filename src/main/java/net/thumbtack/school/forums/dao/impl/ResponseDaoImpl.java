package net.thumbtack.school.forums.dao.impl;

import net.thumbtack.school.forums.dao.ResponseDao;
import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.types.Order;
import net.thumbtack.school.forums.model.view.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ResponseDaoImpl extends BaseDaoImpl implements ResponseDao {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<UserView> getListUserView(boolean superuser) throws ForumException {
        return getListUserView(superuser, null, null);
    }

    @Override
    public List<UserView> getListUserView(boolean superuser, Integer limit, Integer offset) throws ForumException {
        LOGGER.debug("DAO get List UserResponse ");
        try {
            return responseMapper.getListUserView(superuser, limit, offset);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get List UserResponse {}, {}, {}", superuser, limit, offset, ex);
            throw new ForumException();
        }
    }

    @Override
    public List<FullMessageView> getListFullMessageViewByForum(Forum forum, Integer limit, Integer offset, Order order) throws ForumException {
        LOGGER.debug("DAO get List FullMessageView ");
        try {
            return responseMapper.getListFullMessageView(forum, limit, offset, order);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get List FullMessageView {}, {}, {}, {}", forum, limit, offset, ex);
            throw new ForumException();
        }
    }

    @Override
    public List<FullCommentView> getListFullCommentViewByParent(int id, Order order) throws ForumException {
        LOGGER.debug("DAO get List FullCommentView ");
        try {
            return responseMapper.getListFullCommentViewById(id, order);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get List FullCommentView {}, {}, {}", id, order, ex);
            throw new ForumException();
        }
    }

    @Override
    public List<MessageView> getListMessageViewByForum(Forum forum, Integer limit, Integer offset, boolean onlyComment) throws ForumException {
        LOGGER.debug("DAO get List MessageView ");
        try {
            return responseMapper.getListMessageViewSorted(forum, onlyComment, limit, offset);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get List MessageView  {}, {}, {}, {}, {}", forum, limit, offset, onlyComment, ex);
            throw new ForumException();
        }
    }

    @Override
    public List<MessageView> getListMessageViewByForumGroupByCreator(Forum forum, Integer limit, Integer offset) throws ForumException {
        LOGGER.debug("DAO get List MessageView ");
        try {
            return responseMapper.getListMessageViewSortedGroupByCreator(forum, limit, offset);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get List MessageView  {}, {}, {}, {}", forum, limit, offset, ex);
            throw new ForumException();
        }
    }

    @Override
    public List<ForumView> getListForumView(Forum forum, Integer limit, Integer offset) throws ForumException {
        LOGGER.debug("DAO get List ForumView");
        try {
            return responseMapper.getListForumView(forum, limit, offset);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get List ForumView {}, {}, {}, {}", forum, limit, offset, ex);
            throw new ForumException();
        }
    }
}
