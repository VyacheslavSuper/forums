package net.thumbtack.school.forums.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ForumView {
    private String topic;
    private int countMessages;
    private int countComments;
}
