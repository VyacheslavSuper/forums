package net.thumbtack.school.forums.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class FullCommentView {
    private int id;
    private String creator;
    private String unpublishedBody;
    private List<String> publishedBodyes;
    private LocalDateTime timeCreate;
    private double rating;
    private int rated;
    private List<FullCommentView> comments;
}
