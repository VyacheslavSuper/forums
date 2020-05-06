package net.thumbtack.school.forums.mapstruct;


import net.thumbtack.school.forums.dto.response.forum.ForumResponse;
import net.thumbtack.school.forums.dto.response.forum.FullInfoForumResponse;
import net.thumbtack.school.forums.dto.response.statistics.CountMessagesResponse;
import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.view.ForumView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper
public interface ForumMapStruct {

    CountMessagesResponse forumViewToCountMessage(ForumView forumView);

    @Mappings({
            @Mapping(target = "type", source = "forumType")})
    ForumResponse forumToForumResponse(Forum forum);

    @Mappings({
            @Mapping(target = "creator", source = "user.login"),
            @Mapping(target = "type", source = "forumType")})
    FullInfoForumResponse forumToFullInfoForumResponse(Forum forum);
}
