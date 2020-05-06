package net.thumbtack.school.forums.dto.request.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.request.RequestBase;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCommentRequest extends RequestBase {
    @NotNull
    @NotBlank
    private String Body;
}
