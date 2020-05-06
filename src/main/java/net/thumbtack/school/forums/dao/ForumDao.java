package net.thumbtack.school.forums.dao;

import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;

import java.util.List;

public interface ForumDao {

    Forum insert(Forum forum) throws ForumException;

    Forum getById(int id) throws ForumException;

    boolean checkForumById(int id) throws ForumException;

    List<Forum> getAll() throws ForumException;

    List<Forum> getAll(Integer limit, Integer offset) throws ForumException;

    Forum update(Forum forum) throws ForumException;

    void safeDelete(Forum forum) throws ForumException;

    void delete(Forum forum) throws ForumException;
}
