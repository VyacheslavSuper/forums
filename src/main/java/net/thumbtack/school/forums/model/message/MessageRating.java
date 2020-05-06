package net.thumbtack.school.forums.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thumbtack.school.forums.model.User;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageRating {
    private int id;
    private User user;
    private int rating;

    public MessageRating(User user, int rating) {
        this(0, user, rating);
    }

    @Override
    public String toString() {
        return "MessageRating{" +
                "id=" + id +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageRating)) return false;
        MessageRating that = (MessageRating) o;
        return getId() == that.getId() &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getRating(), that.getRating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getRating());
    }
}
