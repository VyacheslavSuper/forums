package net.thumbtack.school.forums.schedulers;

import net.thumbtack.school.forums.dao.UserDao;
import net.thumbtack.school.forums.exception.ForumException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class UnBanUsersTask {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserDao userDao;

    @Scheduled(cron = "${cron.unBan}")
    public void inBanTask() {
        try {
            userDao.unBanUsers(LocalDate.now());
        } catch (ForumException e) {
            LOGGER.info("SCHEDULER can't unBan Users ", e);
        }
    }
}
