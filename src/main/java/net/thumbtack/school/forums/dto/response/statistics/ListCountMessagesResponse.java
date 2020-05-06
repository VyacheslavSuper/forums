package net.thumbtack.school.forums.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.thumbtack.school.forums.dto.response.ResponseBase;

import java.util.List;

@Data
@AllArgsConstructor
public class ListCountMessagesResponse extends ResponseBase {
    private List<CountMessagesResponse> list;
}
