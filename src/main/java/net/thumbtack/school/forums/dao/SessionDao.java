package net.thumbtack.school.forums.dao;

import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Session;


public interface SessionDao {

    Session insert(Session session) throws ForumException;

    Session getById(int id) throws ForumException;

    Session getByLogin(String login) throws ForumException;

    boolean checkSessionByLogin(String login) throws ForumException;

    boolean checkSessionById(int id) throws ForumException;

    boolean checkSessionByCookie(String cookie) throws ForumException;

    Session getByCookie(String cookie) throws ForumException;

    void delete(Session session) throws ForumException;

    void delete(String cookie) throws ForumException;
}
