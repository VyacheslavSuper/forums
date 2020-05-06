package net.thumbtack.school.forums.dao;

import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Session;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.RestrictionType;
import net.thumbtack.school.forums.model.types.UserType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SessionDaoTest extends DaoTestBase {

    @Test
    public void testFullSessionDao() throws ForumException {
        User newuser = new User("TestLogin", "Mypassword23", "MyNames", "testemail", UserType.USER, LocalDate.now(), RestrictionType.FULL, 5, null);
        userDao.insert(newuser);
        User user = userDao.getById(newuser.getId());
        User user2 = userDao.getByLogin(user.getLogin());
        Assert.assertEquals(user, user2);

        Session session = new Session(0, "new-cookie-test", user2);
        Assert.assertFalse(sessionDao.checkSessionByCookie(session.getCookie()));
        sessionDao.insert(session);
        Assert.assertEquals(session, sessionDao.getByLogin(user2.getLogin()));
        Assert.assertTrue(sessionDao.checkSessionByLogin(user2.getLogin()));
        Assert.assertTrue(sessionDao.checkSessionById(session.getId()));
        Assert.assertTrue(sessionDao.checkSessionByCookie(session.getCookie()));
        Session session2 = sessionDao.getById(session.getId());
        Session session3 = sessionDao.getByCookie(session.getCookie());
        Assert.assertEquals(session, session2);
        Assert.assertEquals(session, session3);
        sessionDao.delete(session3);
        Assert.assertNull(sessionDao.getById(session.getId()));
        Assert.assertNull(sessionDao.getByCookie(session.getCookie()));

        //Их, конечно, нужно было сделать на mapper, но что-то пошло не так
        verify(sessionDao).insert(any());
        verify(sessionDao).delete(any(Session.class));
        verify(sessionDao, times(2)).getById(anyInt());
        verify(sessionDao, times(2)).getByCookie(anyString());
    }

}
