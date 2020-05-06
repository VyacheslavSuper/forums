package net.thumbtack.school.forums.dto.request.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.model.validator.NameValid;
import net.thumbtack.school.forums.model.validator.PasswordValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotNull
    @NotBlank
    @NameValid
    private String login;
    @NotNull
    @NotBlank
    @PasswordValid
    private String oldPassword;
    @NotNull
    @NotBlank
    @PasswordValid
    private String password;
}
