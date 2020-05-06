package net.thumbtack.school.forums;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class ApplicationProperties {

    @Value("${banTime}")
    private String banTime;

    @Value("${banCount}")
    private String banCount;

    @Value("${max_name_length}")
    private String maxNameLength;

    @Value("${min_password_length}")
    private String minPasswordLength;

    public String getBanTimeString() {
        return banTime;
    }

    public String getBanCountString() {
        return banCount;
    }

    public String getMaxNameLengthString() {
        return maxNameLength;
    }

    public String getMinPasswordLengthString() {
        return minPasswordLength;
    }

    public Integer getBanTime() {
        return Integer.parseInt(banTime);
    }

    public Integer getBanCount() {
        return Integer.parseInt(banCount);
    }

    public Integer getMaxNameLength() {
        return Integer.parseInt(maxNameLength);
    }

    public Integer getMinPasswordLength() {
        return Integer.parseInt(minPasswordLength);
    }
}
