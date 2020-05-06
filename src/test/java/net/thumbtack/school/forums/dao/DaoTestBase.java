package net.thumbtack.school.forums.dao;

import net.thumbtack.school.forums.ForumsServer;
import net.thumbtack.school.forums.exception.ForumException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ForumsServer.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DaoTestBase {

    @SpyBean
    protected CommonDao commonDao;
    @SpyBean
    protected UserDao userDao;
    @SpyBean
    protected MessageDao messageDao;
    @SpyBean
    protected ForumDao forumDao;
    @SpyBean
    protected SessionDao sessionDao;

    @BeforeEach
    public void clearDatabase() throws ForumException {
        commonDao.clear();
        Mockito.clearInvocations(userDao);
    }

}
