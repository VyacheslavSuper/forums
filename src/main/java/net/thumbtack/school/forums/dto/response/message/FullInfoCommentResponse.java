package net.thumbtack.school.forums.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.response.ResponseBase;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullInfoCommentResponse extends ResponseBase {
    private int id;
    private String creator;
    private List<String> body;
    private LocalDateTime created;
    private Double rating;
    private int rated;
    private List<FullInfoCommentResponse> comments;
}
