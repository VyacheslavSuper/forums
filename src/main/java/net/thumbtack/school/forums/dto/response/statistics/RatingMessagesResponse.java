package net.thumbtack.school.forums.dto.response.statistics;

import lombok.Data;
import net.thumbtack.school.forums.dto.response.ResponseBase;

@Data
public class RatingMessagesResponse extends ResponseBase {
    private String subject;
    private String creator;
    private double rating;
    private int rated;
}
