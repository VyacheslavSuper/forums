package net.thumbtack.school.forums.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.thumbtack.school.forums.dto.response.ResponseBase;

import java.util.List;

@Data
@AllArgsConstructor
public class ListRatingMessagesResponse extends ResponseBase {
    private List<RatingMessagesResponse> list;
}
