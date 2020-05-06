package net.thumbtack.school.forums;

import net.thumbtack.school.forums.dao.UserDao;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.types.RestrictionType;
import net.thumbtack.school.forums.model.types.UserType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FirstStart implements CommandLineRunner {
    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private UserDao userDao;

    @Value("${admin.login}")
    private String adminLogin;
    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    public FirstStart(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userDao.checkUserByLogin(adminLogin)) {
            LOGGER.debug("FIRST START, insert Admin");
            User admin = new User(adminLogin, adminPassword, "Admin", "admin@gmail.com", UserType.SUPERUSER, LocalDate.now(), RestrictionType.FULL, 999);
            LOGGER.info("insert {}", userDao.insert(admin));
        }
    }
}
