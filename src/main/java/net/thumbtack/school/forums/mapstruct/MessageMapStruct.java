package net.thumbtack.school.forums.mapstruct;

import net.thumbtack.school.forums.dto.response.message.FullInfoCommentResponse;
import net.thumbtack.school.forums.dto.response.message.FullInfoMessageResponse;
import net.thumbtack.school.forums.dto.response.statistics.RatingMessagesResponse;
import net.thumbtack.school.forums.dto.response.statistics.RatingUserResponse;
import net.thumbtack.school.forums.model.message.MessageItem;
import net.thumbtack.school.forums.model.view.MessageView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper
public interface MessageMapStruct {

    @Mappings({
            @Mapping(target = "id", source = "message.id"),
            @Mapping(target = "creator", source = "message.user.login"),
            @Mapping(target = "topic", source = "message.messageHeader.topic"),
            @Mapping(target = "priority", source = "message.messageHeader.priority"),
            @Mapping(target = "created", source = "message.timeCreate"),
            @Mapping(target = "body", source = "body"),
            @Mapping(target = "comments", source = "comments"),
            @Mapping(target = "tags", source = "tags")})
    FullInfoMessageResponse messageToFullInfoMessageResponse(MessageItem message, List<String> body, List<String> tags, List<FullInfoCommentResponse> comments);

    @Mappings({
            @Mapping(target="id", source = "message.id"),
            @Mapping(target="creator", source = "message.user.login"),
            @Mapping(target="created", source = "message.timeCreate"),
            @Mapping(target="body", source = "body"),
            @Mapping(target="comments", source = "comments")})
    FullInfoCommentResponse messageToFullInfoCommentResponse(MessageItem message, List<String> body, List<FullInfoCommentResponse> comments);


    RatingUserResponse messageViewToRatingUserResponse(MessageView messageView);

    @Mappings({
            @Mapping(target = "subject", source = "topic")})
    RatingMessagesResponse messageViewToRatingMessageResponse(MessageView messageView);
}