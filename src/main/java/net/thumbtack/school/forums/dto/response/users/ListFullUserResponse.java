package net.thumbtack.school.forums.dto.response.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.thumbtack.school.forums.dto.response.ResponseBase;

import java.util.List;

@Data
@AllArgsConstructor
public class ListFullUserResponse extends ResponseBase {
    List<FullUserResponse> list;
}
