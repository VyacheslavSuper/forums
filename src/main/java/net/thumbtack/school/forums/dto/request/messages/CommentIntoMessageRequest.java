package net.thumbtack.school.forums.dto.request.messages;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.request.RequestBase;
import net.thumbtack.school.forums.model.types.MessagePriority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CommentIntoMessageRequest extends RequestBase {
    @NotNull
    @NotBlank
    private String topic;
    private MessagePriority priority;
    private List<String> tags;

    public CommentIntoMessageRequest(String topic) {
        this(topic, MessagePriority.NORMAL, new ArrayList<>());
    }

    public CommentIntoMessageRequest(String topic, MessagePriority priority, List<String> tags) {
        this.topic = topic;
        this.priority = priority;
        this.tags = tags;
        if (priority == null) {
            this.priority = MessagePriority.NORMAL;
        }
        if (tags == null) {
            this.tags = new ArrayList<>();
        }
    }
}
