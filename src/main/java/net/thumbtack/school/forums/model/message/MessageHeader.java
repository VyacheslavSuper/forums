package net.thumbtack.school.forums.model.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.types.MessagePriority;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class MessageHeader {
    private int id;
    private MessagePriority priority;
    private String topic;

    private Forum forum;
    private MessageItem root;
    private List<MessageTag> messageTags;

    private MessageHeader(int id, MessagePriority priority, String topic, Forum forum, MessageItem root, List<MessageTag> messageTags) {
        this.id = id;
        this.priority = priority;
        this.topic = topic;
        this.forum = forum;
        this.root = root;
        this.messageTags = messageTags;
    }

    public MessageHeader(MessagePriority priority, String topic, Forum forum, MessageItem root, List<MessageTag> messageTags) {
        this(0, priority, topic, forum, root, messageTags);
    }

    @Override
    public String toString() {
        return "MessageHeader{" +
                "id=" + id +
                ", priority=" + priority +
                ", topic='" + topic + '\'' +
                ", forum=" + forum +
                ", root=" + root +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageHeader)) return false;
        MessageHeader that = (MessageHeader) o;
        return getId() == that.getId() &&
                getPriority() == that.getPriority() &&
                Objects.equals(getTopic(), that.getTopic()) &&
                Objects.equals(getForum(), that.getForum()) &&
                Objects.equals(getRoot(), that.getRoot()) &&
                getMessageTags().containsAll(that.getMessageTags());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPriority(), getTopic(), getForum(), getRoot(), getMessageTags());
    }
}
