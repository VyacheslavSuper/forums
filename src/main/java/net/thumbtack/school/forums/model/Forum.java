package net.thumbtack.school.forums.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thumbtack.school.forums.model.message.MessageHeader;
import net.thumbtack.school.forums.model.message.MessageItem;
import net.thumbtack.school.forums.model.types.ForumType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class Forum {
    private int id;
    private User user;
    private String topic;
    private ForumType forumType;
    private Boolean readonly;

    private List<MessageHeader> messages;
    private int countMessages;
    private int countComments;
    private List<MessageItem> allMessageItems;

    public Forum(int id, User user, String topic, ForumType forumType, Boolean readonly, List<MessageHeader> messages) {
        this.id = id;
        this.user = user;
        this.topic = topic;
        this.forumType = forumType;
        this.readonly = readonly;
        this.messages = messages;
    }

    public Forum(int id, User user, String topic, ForumType forumType) {
        this(id, user, topic, forumType, false, new ArrayList<>());
    }

    public Forum(User user, String topic, ForumType forumType) {
        this(0, user, topic, forumType, false, new ArrayList<>());
    }

    @Override
    public String toString() {
        return "Forum{" +
                "id=" + id +
                ", user=" + user +
                ", topic='" + topic + '\'' +
                ", forumType=" + forumType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Forum)) return false;
        Forum forum = (Forum) o;
        return getId() == forum.getId() &&
                Objects.equals(getUser(), forum.getUser()) &&
                Objects.equals(getTopic(), forum.getTopic()) &&
                getForumType() == forum.getForumType() &&
                Objects.equals(getReadonly(), forum.getReadonly());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser(), getTopic(), getForumType(), getReadonly());
    }
}
