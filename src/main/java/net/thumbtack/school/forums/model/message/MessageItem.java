package net.thumbtack.school.forums.model.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thumbtack.school.forums.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class MessageItem {
    private int id;
    private LocalDateTime timeCreate;

    private User user;
    private MessageItem parent;
    private List<MessageBody> messageBodyList;
    private List<MessageRating> messageRatingList;
    private List<MessageItem> messages;

    private double rating;
    private int rated;
    private int countComments;
    private MessageHeader messageHeader;

    public MessageItem(LocalDateTime timeCreate, User user, MessageItem parent, List<MessageBody> messageBodyList) {
        this(0, timeCreate, user, parent, messageBodyList, new ArrayList<>(), new ArrayList<>());
    }

    public MessageItem(int id, LocalDateTime timeCreate, User user, MessageItem parent, List<MessageBody> messageBodyList, List<MessageRating> messageRatingList, List<MessageItem> messages) {
        this.id = id;
        this.timeCreate = timeCreate;
        this.user = user;
        this.parent = parent;
        this.messageBodyList = messageBodyList;
        this.messageRatingList = messageRatingList;
        this.messages = messages;
    }
    @Override
    public String toString() {
        return "MessageItem{" +
                "id=" + id +
                ", timeCreate=" + timeCreate +
                ", user=" + user +
                ", parent=" + parent +
                ", messageBodyList=" + messageBodyList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageItem)) return false;
        MessageItem that = (MessageItem) o;
        return getId() == that.getId() &&
                Objects.equals(getTimeCreate(), that.getTimeCreate()) &&
                Objects.equals(getMessageBodyList(), that.getMessageBodyList()) &&
                Objects.equals(getMessageRatingList(), that.getMessageRatingList()) &&
                Objects.equals(getMessages(), that.getMessages());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTimeCreate(), getMessageBodyList(), getMessageRatingList(), getMessages());
    }
}
