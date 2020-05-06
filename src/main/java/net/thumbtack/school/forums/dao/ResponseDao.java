package net.thumbtack.school.forums.dao;

import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.types.Order;
import net.thumbtack.school.forums.model.view.*;

import java.util.List;

public interface ResponseDao {

    List<UserView> getListUserView(boolean superuser) throws ForumException;

    List<UserView> getListUserView(boolean superuser, Integer limit, Integer offset) throws ForumException;

    List<FullMessageView> getListFullMessageViewByForum(Forum forum, Integer limit, Integer offset, Order order) throws ForumException;

    List<FullCommentView> getListFullCommentViewByParent(int id, Order order) throws ForumException;

    List<MessageView> getListMessageViewByForum(Forum forum, Integer limit, Integer offset, boolean onlyComment) throws ForumException;

    List<MessageView> getListMessageViewByForumGroupByCreator(Forum forum, Integer limit, Integer offset) throws ForumException;

    List<ForumView> getListForumView(Forum forum, Integer limit, Integer offset) throws ForumException;
}
