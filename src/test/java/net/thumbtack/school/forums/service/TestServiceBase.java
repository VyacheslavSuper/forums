package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.ApplicationProperties;
import net.thumbtack.school.forums.dao.*;
import net.thumbtack.school.forums.exception.ForumException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TestServiceBase {

    @Autowired
    protected UserService userService;
    @Autowired
    protected SessionService sessionService;
    @Autowired
    protected SettingsService settingsService;
    @Autowired
    protected ForumService forumService;
    @Autowired
    protected MessageService messageService;
    @Autowired
    protected StatisticsService statisticsService;

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
    @SpyBean
    protected ResponseDao responseDao;

    @SpyBean
    protected ApplicationProperties properties;

    @BeforeEach
    public void clearDatabase() throws ForumException {
        commonDao.clear();
        Mockito.clearInvocations(userDao);
    }

}
