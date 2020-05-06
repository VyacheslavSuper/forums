package net.thumbtack.school.forums.dto.request.messages;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.model.types.MessagePriority;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateMessageRequest {
    @NotNull
    @NotBlank
    private String topic;
    @NotNull
    @NotBlank
    private String body;
    private MessagePriority priority;
    private List<String> tags;

    public CreateMessageRequest(String topic, String body, MessagePriority priority, List<String> tags) {
        this.topic = topic;
        this.body = body;
        this.priority = priority;
        this.tags = tags;
        if (priority == null) {
            this.priority = MessagePriority.NORMAL;
        }
        if (tags == null) {
            this.tags = new ArrayList<>();
        }
    }

    public CreateMessageRequest(String topic, String body) {
        this(topic, body, MessagePriority.NORMAL, new ArrayList<>());
    }
}
