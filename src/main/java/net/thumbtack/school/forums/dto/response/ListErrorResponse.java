package net.thumbtack.school.forums.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListErrorResponse extends ResponseBase {
    private List<ErrorResponse> errors;
}
