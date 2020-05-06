package net.thumbtack.school.forums.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageTag {
    private int id;
    private String tag;

    public MessageTag(String tag) {
        this(0, tag);
    }
}
