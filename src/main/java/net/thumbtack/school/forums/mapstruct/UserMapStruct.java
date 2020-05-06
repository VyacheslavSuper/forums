package net.thumbtack.school.forums.mapstruct;


import net.thumbtack.school.forums.dto.request.users.RegisterUserRequest;
import net.thumbtack.school.forums.dto.response.users.FullUserResponse;
import net.thumbtack.school.forums.dto.response.users.UserResponse;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.view.UserView;
import org.mapstruct.Mapper;


@Mapper
public interface UserMapStruct {

    UserResponse userToUserResponse(User user);

    User userFromRegisterRequest(RegisterUserRequest request);

    FullUserResponse userViewToFullUserResponse(UserView userView);
}
