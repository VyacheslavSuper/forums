package net.thumbtack.school.forums.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.thumbtack.school.forums.dto.response.ResponseBase;

import java.util.List;

@Data
@AllArgsConstructor
public class ListFullInfoMessageResponse extends ResponseBase {
    private List<FullInfoMessageResponse> list;
}
