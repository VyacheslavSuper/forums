package net.thumbtack.school.forums.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.model.types.MessageState;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageBody {
    private int id;
    private MessageState state;
    private String body;

    public MessageBody(MessageState state, String body) {
        this(0, state, body);
    }
}
