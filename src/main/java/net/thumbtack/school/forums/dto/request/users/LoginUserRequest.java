package net.thumbtack.school.forums.dto.request.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.request.RequestBase;
import net.thumbtack.school.forums.model.validator.NameValid;
import net.thumbtack.school.forums.model.validator.PasswordValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserRequest extends RequestBase {
    //в sql уже есть и login и name, они имеют разный смысл. name будет, как login, а userName, как name
    //INTO RegisterUserRequest.class
    @NotNull
    @NotBlank
    @NameValid
    private String login;
    @NotNull
    @NotBlank
    @PasswordValid
    private String password;
}
