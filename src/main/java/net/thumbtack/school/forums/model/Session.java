package net.thumbtack.school.forums.model;

import lombok.*;

import java.util.Objects;

@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Session {
    private int id;
    private String cookie;
    private User user;

    public Session(String cookie, User user) {
        this(0, cookie, user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Session)) return false;
        Session session = (Session) o;
        return getId() == session.getId() &&
                Objects.equals(getCookie(), session.getCookie());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCookie());
    }
}
