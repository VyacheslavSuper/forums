package net.thumbtack.school.forums.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.exception.ForumException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse extends ResponseBase {
    private String errorCode;
    private String field;
    private String message;

    public ErrorResponse(ForumException forumException) {
        this.errorCode = forumException.getError().getErrorCode();
        this.field = forumException.getError().getField();
        this.message = forumException.getError().getMessage();
    }
}
