package net.thumbtack.school.forums.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.response.ResponseBase;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountMessagesResponse extends ResponseBase {
    private String topic;
    private int countMessages;
    private int countComments;
}
