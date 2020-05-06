package net.thumbtack.school.forums.dto.response.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.response.ResponseBase;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse extends ResponseBase {
    private int id;
    private String login;
    private String email;
}
