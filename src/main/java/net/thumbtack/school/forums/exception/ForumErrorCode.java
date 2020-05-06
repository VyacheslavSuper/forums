package net.thumbtack.school.forums.exception;

import lombok.Data;

public enum ForumErrorCode {
    UNKNOWN_ERROR("Unknown error", "UNKNOWN_ERROR", "error"),
    DATABASE_ERROR("Internal Server Error", "DATABASE_ERROR", "server"),

    REQUEST_NOT_VALID("%s", "REQUEST_NOT_VALID", "%s"),

    BAD_REQUEST("Bad request", "BAD_REQUEST", "request"),

    INTERNAL_SERVER_ERROR("Internal server error", "INTERNAL_SERVER_ERROR", "server"),
    WRONG_DEBUG_MAPPER("Internal Server Error", "INTERNAL_SERVER_ERROR", "server"),

    NOT_ENOUGH_RIGHTS("You are not a SuperUser", "NOT_ENOUGH_RIGHTS", "superuser"),
    ALREADY_SUPERUSER("This user is already SuperUser", "ALREADY_SUPERUSER", "superuser"),

    NO_COOKIE("You do not have cookies", "NO_COOKIE", "cookie"),

    MESSAGEBODY_FULL_PUBLISHED("Message %s has no unpublished posts", "MESSAGE_FULL_PUBLISHED", "message"),
    USER_NOT_CREATER("You are not the Creator", "USER_NOT_CREATER", "forum"),
    DELETE_MESSAGE_IMPOSSIBLE("You cannot delete %s the message", "UDELETE_MESSAGE_IMPOSSIBLE", "message"),
    CONVERSION_COMMENT_IMPOSSIBLE("It is impossible to convert comment %s в message", "CONVERSION_COMMENT_IMPOSSIBLE", "message"),
    MESSAGE_RATING_CANNOT_BE_CHANGED("Rating cannot be changed on message %s", "MESSAGE_NOT_FOUND", "message"),
    MESSAGE_NOT_FOUND("Message %s not found", "MESSAGE_NOT_FOUND", "message"),

    PRIORITY_MATCHES("The old priority is the same as the new one", "PRIORITY_MATCHES", "message"),

    FORUM_UNMODERATED("Forum %s is unModerated", "FORUM_UNMODERATED", "forum"),
    FORUM_DUPLICATE("Forum with %s duplicated", "FORUM_DUPLICATE", "forum"),
    FORUM_NOT_FOUND("Forum %s not found", "FORUM_NOT_FOUND", "forum"),
    FORUM_IS_READONLY("Forum %s is readonly", "FORUM_IS_READONLY", "forum"),

    USER_DELETED("User is deleted", "USER_DELETED", "user"),
    USER_BANNED("User is banned", "USER_BANNED", "user"),
    USER_MATCHES("User matches", "USER_MATCHES", "user"),
    USER_AUTHORIZED("User is already authorized", "USER_AUTHORIZED", "user"),
    USER_UNAUTHORIZED("User not yet authorized", "USER_UNAUTHORIZED", "user"),
    USER_ID_NOT_FOUND("User with id %s not found", "USER_ID_NOT_FOUND", "user"),
    LOGIN_NOT_FOUND("User with this login %s not found", "LOGIN_NOT_FOUND", "login"),
    LOGIN_ALREADY_EXISTS("User %s already exists", "LOGIN_ALREADY_EXISTS", "login"),
    LOGIN_OR_PASSWORD_INVALID("login or password invalid", "LOGIN_OR_PASSWORD_INVALID", "login or password");
    private String format;
    private String message;
    private String errorCode;
    private String field;

    ForumErrorCode(String format, String errorCode, String field) {
        this.format = format;
        this.errorCode = errorCode;
        this.field = field;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }

    public String getFormat() {
        return format;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "ForumErrorCode{" +
                "format='" + format + '\'' +
                ", message='" + message + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", field='" + field + '\'' +
                '}';
    }
}
