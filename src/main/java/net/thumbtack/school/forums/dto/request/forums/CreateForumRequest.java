package net.thumbtack.school.forums.dto.request.forums;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.request.RequestBase;
import net.thumbtack.school.forums.model.types.ForumType;
import net.thumbtack.school.forums.model.validator.NameValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CreateForumRequest extends RequestBase {
    @NotNull
    @NotBlank
    @NameValid
    private String topic;
    private ForumType forumType;

    public CreateForumRequest(String topic, ForumType forumType) {
        this.topic = topic;
        this.forumType = forumType;
        if (forumType == null) {
            this.forumType = ForumType.UNMODERATED;
        }
    }
}
