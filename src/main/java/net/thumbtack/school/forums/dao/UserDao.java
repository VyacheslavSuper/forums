package net.thumbtack.school.forums.dao;

import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.RestrictionType;

import java.time.LocalDate;
import java.util.List;

public interface UserDao {

    User insert(User user) throws ForumException;

    User getById(int id) throws ForumException;

    User getByLogin(String login) throws ForumException;

    boolean checkUserById(int id) throws ForumException;

    boolean checkUserByLogin(String login) throws ForumException;

    List<User> getAll() throws ForumException;

    List<User> getAll(Integer limit, Integer offset) throws ForumException;

    List<User> getUsersByRestriction(RestrictionType restrictionType, Integer limit, Integer offset) throws ForumException;

    List<User> getUsersByRestriction(RestrictionType restrictionType) throws ForumException;

    User update(User user) throws ForumException;

    void unBanUsers(LocalDate time) throws ForumException;

    void safeDelete(User user) throws ForumException;

    void delete(User user) throws ForumException;

}
