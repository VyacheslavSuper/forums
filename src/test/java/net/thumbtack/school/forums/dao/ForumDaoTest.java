package net.thumbtack.school.forums.dao;

import net.thumbtack.school.forums.exception.ForumException;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.ForumType;
import net.thumbtack.school.forums.model.types.RestrictionType;
import net.thumbtack.school.forums.model.types.UserType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ForumDaoTest extends DaoTestBase {

    @Test
    public void testfullForumDao() throws ForumException {
        User newuser = new User("TestLogin3", "Mypassword23", "MyNames", "testemail", UserType.USER, LocalDate.now(), RestrictionType.FULL, 5, null);
        userDao.insert(newuser);
        Forum forum = new Forum(newuser, "MyTopic", ForumType.UNMODERATED);
        forumDao.insert(forum);
        Assert.assertTrue(forumDao.checkForumById(forum.getId()));
        Forum getforum = forumDao.getById(forum.getId());
        Assert.assertEquals(forum, getforum);
        forum.setForumType(ForumType.MODERATED);
        forum.setTopic("OtherMyTopic");
        forumDao.update(forum);
        Assert.assertEquals(forum, forumDao.getById(forum.getId()));
        Assert.assertEquals(1, forumDao.getAll().size());
        Assert.assertEquals(1, userDao.getById(newuser.getId()).getForums().size());
        forumDao.delete(forum);
        Assert.assertEquals(0, forumDao.getAll().size());
        Assert.assertEquals(0, userDao.getById(newuser.getId()).getForums().size());
        Assert.assertNull(forumDao.getById(forum.getId()));

        verify(forumDao).insert(any());
        verify(forumDao).delete(any());
        verify(forumDao, times(2)).getAll();
        verify(forumDao).update(any());
        verify(forumDao, times(3)).getById(anyInt());
    }

    @Test
    public void testSafeDelete() throws ForumException {
        User newuser = new User("TestLogin2", "Mypassword23", "MyNames", "testemail", UserType.USER, LocalDate.now(), RestrictionType.FULL, 5, null);
        userDao.insert(newuser);

        Forum forum = new Forum(newuser, "MyTopic", ForumType.UNMODERATED);
        forumDao.insert(forum);
        Assert.assertFalse(forum.getReadonly());
        Assert.assertFalse(forumDao.getById(forum.getId()).getReadonly());

        forumDao.safeDelete(forum);
        Assert.assertTrue(forum.getReadonly());
        Assert.assertTrue(forumDao.getById(forum.getId()).getReadonly());
    }

}
