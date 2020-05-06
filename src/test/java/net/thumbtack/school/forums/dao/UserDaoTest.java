package net.thumbtack.school.forums.dao;

import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.RestrictionType;
import net.thumbtack.school.forums.model.types.UserType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

public class UserDaoTest extends DaoTestBase {

    @Test
    public void testFullUserDao() throws ForumException {
        User newuser = new User("TestLogin2", "Mypassword23", "MyNames", "testemail", UserType.USER, LocalDate.now(), RestrictionType.FULL, 5, null);
        Assert.assertFalse(userDao.checkUserByLogin(newuser.getLogin()));
        userDao.insert(newuser);

        Assert.assertTrue(userDao.checkUserByLogin("TestLogin2"));
        Assert.assertTrue(userDao.checkUserById(newuser.getId()));
        Assert.assertEquals(newuser, userDao.getById(newuser.getId()));
        Assert.assertEquals(newuser, userDao.getByLogin(newuser.getLogin()));
        Assert.assertEquals(1, userDao.getAll().size());
        newuser.setName("OtherMyName");
        newuser.setEmail("testemail@gmail.com");
        userDao.update(newuser);
        Assert.assertEquals(newuser, userDao.getById(newuser.getId()));
        Assert.assertEquals(newuser, userDao.getByLogin(newuser.getLogin()));
        userDao.delete(newuser);
        Assert.assertNull(userDao.getById(newuser.getId()));
        Assert.assertNull(userDao.getByLogin(newuser.getLogin()));
        Assert.assertEquals(0, userDao.getAll().size());

        verify(userDao).insert(newuser);
        verify(userDao, Mockito.times(3)).getById(anyInt());
        verify(userDao, Mockito.times(3)).getByLogin(anyString());
        verify(userDao).update(any());
        verify(userDao).delete(any());
    }

    @Test
    public void testSafeDelete() throws ForumException {
        User newuser = new User("TestLogin2", "Mypassword23", "MyNames", "testemail", UserType.USER, LocalDate.now(), RestrictionType.FULL, 5, null);
        userDao.insert(newuser);
        Assert.assertEquals(newuser, userDao.getById(newuser.getId()));
        Assert.assertFalse(newuser.getDeleted());
        Assert.assertFalse(userDao.getById(newuser.getId()).getDeleted());
        userDao.safeDelete(newuser);
        Assert.assertEquals(newuser, userDao.getById(newuser.getId()));
        Assert.assertTrue(newuser.getDeleted());
        Assert.assertTrue(userDao.getById(newuser.getId()).getDeleted());
    }

}
