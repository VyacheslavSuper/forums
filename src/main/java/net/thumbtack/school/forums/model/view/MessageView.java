package net.thumbtack.school.forums.model.view;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageView {
    private String topic;
    private double rating;
    private int rated;
    private String creator;
}
