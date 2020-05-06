package net.thumbtack.school.forums.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.thumbtack.school.forums.dto.response.ResponseBase;

import java.util.List;

@Data
@AllArgsConstructor
public class ListRatingUserResponse extends ResponseBase {
    private List<RatingUserResponse> list;
}
