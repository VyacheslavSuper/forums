package net.thumbtack.school.forums.dto.response.statistics;

import lombok.Data;
import net.thumbtack.school.forums.dto.response.ResponseBase;

@Data
public class RatingUserResponse extends ResponseBase {
    private String creator;
    private double rating;
    private int rated;
}
