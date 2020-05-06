package net.thumbtack.school.forums.exception;

import net.thumbtack.school.forums.dto.response.ErrorResponse;
import net.thumbtack.school.forums.dto.response.ListErrorResponse;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private ObjectError objectError;

    @ExceptionHandler({SQLException.class, DataAccessException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseBase dataBaseError() {
        return new ErrorResponse(new ForumException());
    }

    @ExceptionHandler(ForumException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseBase handleForumError(HttpServletRequest req, ForumException exception) {
        return new ErrorResponse(exception);
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseBase handleNullPointerError() {
        return new ErrorResponse(new ForumException(ForumErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseBase handleNotValidError(MethodArgumentNotValidException exception) {
        List<ErrorResponse> errors = new ArrayList<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errors.add(new ErrorResponse(fieldError.getDefaultMessage(), ForumErrorCode.REQUEST_NOT_VALID.getErrorCode(), fieldError.getField()));
        }
        return new ListErrorResponse(errors);
    }

}
