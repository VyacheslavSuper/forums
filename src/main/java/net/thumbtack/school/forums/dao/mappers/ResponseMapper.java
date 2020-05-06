package net.thumbtack.school.forums.dao.mappers;

import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.types.Order;
import net.thumbtack.school.forums.model.view.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ResponseMapper {
    @Select({"<script>",
            "SELECT * FROM ",
            "<if test='super == true'> `userview_super` ",
            "</if>",
            "<if test='super == false'> `userview` ",
            "</if>",
            " ORDER BY `id` ASC ",
            "<if test='limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test='offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    List<UserView> getListUserView(@Param("super") boolean superUser, @Param("limit") Integer limit, @Param("offset") Integer offset);


    @Select({"<script>",
            "SELECT * FROM `fullmessageview` WHERE `forumid` = #{forum.id} AND ISNULL(`parent`) " +
                    "ORDER BY `priority` ASC  ",
            "<if test='order == @net.thumbtack.school.forums.model.types.Order@ASC '> , `timeCreate` ASC ",
            "</if>",
            "<if test='order == @net.thumbtack.school.forums.model.types.Order@DESC '> , `timeCreate` DESC ",
            "</if>",
            "<if test='limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test='offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "unpublishedBody", column = "id", javaType = String.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ResponseMapper.getUnpublishedBody", fetchType = FetchType.LAZY)),
            @Result(property = "publishedBodyes", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.ResponseMapper.getPublishedListBody", fetchType = FetchType.LAZY)),
            @Result(property = "tags", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.ResponseMapper.getListTags", fetchType = FetchType.LAZY)),
    })
    List<FullMessageView> getListFullMessageView(@Param("forum") Forum forum, @Param("limit") Integer limit, @Param("offset") Integer offset, @Param("order") Order order);

    @Select({"<script>",
            "SELECT * FROM `fullmessageview` WHERE `parent` = #{id} ",
            "<if test='order == @net.thumbtack.school.forums.model.types.Order@ASC '> ORDER BY `timeCreate` ASC ",
            "</if>",
            "<if test='order == @net.thumbtack.school.forums.model.types.Order@DESC '> ORDER BY `timeCreate` DESC ",
            "</if>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "unpublishedBody", column = "id", javaType = String.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ResponseMapper.getUnpublishedBody", fetchType = FetchType.LAZY)),
            @Result(property = "publishedBodyes", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.ResponseMapper.getPublishedListBody", fetchType = FetchType.LAZY)),
    })
    List<FullCommentView> getListFullCommentViewById(int id, @Param("order") Order order);

    @Select("SELECT `body` AS `unpublishedBody` FROM `message_text` WHERE `infoid` = #{id} AND `state` = 'UNPUBLISHED' ")
    String getUnpublishedBody(int id);

    @Select("SELECT `body` AS `publishedBodyes` FROM `message_text` WHERE `infoid` = #{id} AND `state` = 'PUBLISHED' " +
            "ORDER BY `state` DESC, `message_text`.`id` DESC ")
    List<String> getPublishedListBody(int id);

    @Select("SELECT `tag` FROM `message_tags` WHERE `header` = #{id} ")
    List<String> getListTags(int id);

    @Select({"<script>",
            "SELECT * FROM `fullmessageview` WHERE TRUE ",
            "<if test='forum != null'> AND `forumid` = #{forum.id} ",
            "</if>",
            "<if test='onlyComment == true'> AND `parent` ",
            "</if>",
            "<if test='onlyComment == false'> AND ISNULL(`parent`) ",
            "</if>",
            " ORDER BY `rating` DESC ",
            "<if test='limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test='offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    List<MessageView> getListMessageViewSorted(@Param("forum") Forum forum, @Param("onlyComment") boolean onlyComment, @Param("limit") Integer limit, @Param("offset") Integer offset);

    //SELECT * FROM `fullmessageview` WHERE TRUE GROUP BY `rating`  ORDER BY `rating` DESC
    @Select({"<script>",
            "SELECT DISTINCT `creator`, `rating`, `rated` FROM `fullmessageview` WHERE TRUE ",
            "<if test='forum != null'> AND `forumid` = #{forum.id} ",
            "</if>",
            " ORDER BY `rating` DESC ",
            "<if test='limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test='offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    List<MessageView> getListMessageViewSortedGroupByCreator(@Param("forum") Forum forum, @Param("limit") Integer limit, @Param("offset") Integer offset);


    @Select({"<script>",
            "SELECT * FROM `forumview` ",
            "<if test='forum != null'> WHERE `forumid` = #{forum.id} ",
            "</if>",
            " ORDER BY `countMessages` DESC, `countComments` DESC ",
            "<if test='limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test='offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    List<ForumView> getListForumView(@Param("forum") Forum forum, @Param("limit") Integer limit, @Param("offset") Integer offset);
}
