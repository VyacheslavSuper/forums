package net.thumbtack.school.forums.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.model.types.MessagePriority;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FullInfoMessageResponse extends ResponseBase {
    private int id;
    private String creator;
    private String topic;
    private List<String> body;
    private MessagePriority priority;
    private List<String> tags;
    private LocalDateTime created;
    private Double rating;
    private int rated;
    private List<FullInfoCommentResponse> comments;
}
