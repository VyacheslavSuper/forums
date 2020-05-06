package net.thumbtack.school.forums.dto.request.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.request.RequestBase;
import net.thumbtack.school.forums.model.types.MessagePriority;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePriorityRequest extends RequestBase {
    @NotNull
    private MessagePriority priority;
}
