package net.thumbtack.school.forums.dto.request.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.request.RequestBase;
import net.thumbtack.school.forums.model.types.Decision;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishMessageRequest extends RequestBase {
    @NotNull
    private Decision decision;
}
