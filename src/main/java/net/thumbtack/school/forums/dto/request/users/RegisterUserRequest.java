package net.thumbtack.school.forums.dto.request.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.request.RequestBase;
import net.thumbtack.school.forums.model.validator.NameValid;
import net.thumbtack.school.forums.model.validator.PasswordValid;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest extends RequestBase {
    @NotNull
    @NotBlank
    @NameValid
    private String login;
    private String name;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    @PasswordValid
    private String password;

    public RegisterUserRequest(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
    }
}
