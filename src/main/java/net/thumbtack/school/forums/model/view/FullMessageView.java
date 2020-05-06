package net.thumbtack.school.forums.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.model.types.MessagePriority;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class FullMessageView {
    private int id;
    private String creator;
    private String topic;
    private String unpublishedBody;
    private List<String> publishedBodyes;
    private MessagePriority priority;
    private List<String> tags;
    private LocalDateTime timeCreate;
    private double rating;
    private int rated;
    private List<FullCommentView> comments;
}
