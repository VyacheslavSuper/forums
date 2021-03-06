package net.thumbtack.school.forums.dto.response.forum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.model.types.ForumType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullInfoForumResponse extends ResponseBase {
    private int id;
    private String topic;
    private ForumType type;
    private String creator;
    private Boolean readonly;
    private int countMessages;
    private int countComments;
}
