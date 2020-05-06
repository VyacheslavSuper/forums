package net.thumbtack.school.forums.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.model.types.MessageState;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse extends ResponseBase {
    private int id;
    private MessageState state;
}
