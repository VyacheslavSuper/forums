package net.thumbtack.school.forums.controllers.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.thumbtack.school.forums.ForumsServer;
import net.thumbtack.school.forums.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {ForumsServer.class})
public class TestBaseMvc {

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @MockBean
    protected UserService userService;
    @MockBean
    protected CookieService cookieService;
    @MockBean
    protected SettingsService settingsService;
    @MockBean
    protected SessionService sessionService;
    @MockBean
    protected MessageService messageService;
    @MockBean
    protected ForumService forumService;
    @MockBean
    protected DebugService debugService;
    @MockBean
    protected StatisticsService statisticsService;

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCookie() {
        Assert.assertNotNull(addTestCookie());
        Assert.assertNotNull(delTestCookie());
    }

    protected Cookie addTestCookie() {
        Cookie cookie = new Cookie("JAVASESSIONID", "my_cookie");
        cookie.setMaxAge(60 * 60);
        return cookie;
    }

    protected Cookie delTestCookie() {
        Cookie cookie = new Cookie("JAVASESSIONID", null);
        cookie.setMaxAge(0);
        return cookie;
    }
}
