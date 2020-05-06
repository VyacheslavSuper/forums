package net.thumbtack.school.forums.dto.request.messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.forums.dto.request.RequestBase;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddRatingOnMessageRequest extends RequestBase {
    @Min(1)
    @Max(5)
    private Integer value;
}
