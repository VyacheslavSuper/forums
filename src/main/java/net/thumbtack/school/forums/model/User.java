package net.thumbtack.school.forums.model;

import lombok.Getter;
import lombok.Setter;
import net.thumbtack.school.forums.model.message.MessageItem;
import net.thumbtack.school.forums.model.types.RestrictionType;
import net.thumbtack.school.forums.model.types.UserType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class User {
    private int id;
    private String login;
    private String password;
    private String name;
    private String email;
    private UserType userType;
    private LocalDate timeRegistered;
    private RestrictionType restrictionType;
    private int banCount;
    private LocalDate banTime;
    private Boolean deleted;

    private List<Forum> forums;
    private List<MessageItem> messages;

    public User() {
        this.id = 0;
        this.userType = UserType.USER;
        this.timeRegistered = LocalDate.now();
        this.restrictionType = RestrictionType.FULL;
        this.banCount = 0;
        this.deleted = false;
        this.forums = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public User(int id, String login, String password, String name, String email, UserType userType, LocalDate timeRegistered, RestrictionType restrictionType, int banCount, LocalDate banTime, Boolean deleted, List<Forum> forums, List<MessageItem> messages) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.timeRegistered = timeRegistered;
        this.restrictionType = restrictionType;
        this.banCount = banCount;
        this.banTime = banTime;
        this.deleted = deleted;
        this.forums = forums;
        this.messages = messages;
    }

    public User(int id, String login, String password, String name, String email, UserType userType, LocalDate timeRegistered, RestrictionType restrictionType, int banCount, LocalDate banTime) {
        this(id, login, password, name, email, userType, timeRegistered, restrictionType, banCount, banTime, false, new ArrayList<>(), new ArrayList<>());
    }

    public User(String login, String password, String name, String email, UserType userType, LocalDate timeRegistered, RestrictionType restrictionType, int banCount, LocalDate banTime) {
        this(0, login, password, name, email, userType, timeRegistered, restrictionType, banCount, banTime, false, new ArrayList<>(), new ArrayList<>());
    }

    public User(String login, String password, String name, String email, UserType userType, LocalDate timeRegistered, RestrictionType restrictionType, int banCount) {
        this(0, login, password, name, email, userType, timeRegistered, restrictionType, banCount, null, false, new ArrayList<>(), new ArrayList<>());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", userType=" + userType +
                ", timeRegistered=" + timeRegistered +
                ", restrictionType=" + restrictionType +
                ", banCount=" + banCount +
                ", banTime=" + banTime +
                ", deleted=" + deleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                getBanCount() == user.getBanCount() &&
                Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                getUserType() == user.getUserType() &&
                Objects.equals(getTimeRegistered(), user.getTimeRegistered()) &&
                getRestrictionType() == user.getRestrictionType() &&
                Objects.equals(getBanTime(), user.getBanTime()) &&
                Objects.equals(getDeleted(), user.getDeleted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin(), getPassword(), getName(), getEmail(), getUserType(), getTimeRegistered(), getRestrictionType(), getBanCount(), getBanTime(), getDeleted());
    }
}
