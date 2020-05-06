package net.thumbtack.school.forums.exception;

import lombok.Data;

@Data
public class ForumException extends Exception {
    private ForumErrorCode forumErrorCode;

    public ForumException(String message, ForumErrorCode forumErrorCode, String field) {
        this.forumErrorCode = forumErrorCode;
        if (message != null && message.length() > 0) {
            forumErrorCode.setMessage(String.format(forumErrorCode.getFormat(), message));
        }
        if (field != null && field.length() > 0) {
            forumErrorCode.setField(String.format(forumErrorCode.getField(), field));
        }
    }

    public ForumException(String message, ForumErrorCode forumErrorCode) {
        this(message, forumErrorCode, null);
    }

    public ForumException(int message, ForumErrorCode forumErrorCode) {
        this(String.valueOf(message), forumErrorCode, null);
    }

    public ForumException(ForumErrorCode forumErrorCode) {
        this(null, forumErrorCode, null);
    }

    public ForumException() {
        this(ForumErrorCode.DATABASE_ERROR);
    }

    public ForumErrorCode getError() {
        return forumErrorCode;
    }

}
