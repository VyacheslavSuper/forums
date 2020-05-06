package net.thumbtack.school.forums.dao.mappers;

import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.message.*;
import net.thumbtack.school.forums.model.types.MessageState;
import net.thumbtack.school.forums.model.types.Order;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface MessageMapper {

    //-----------------INSERT------------------------------------------------------
    @Insert("INSERT INTO `message_header` ( `forumid`, `priority`, `topic` ) " +
            "VALUES (#{header.forum.id} , #{header.priority} , #{header.topic} )")
    @Options(useGeneratedKeys = true, keyProperty = "header.id")
    Integer insertHeader(@Param("header") MessageHeader header);

    @Insert({"<script>",
            "INSERT INTO `message_info` (`header`,`userid`, `timeCreate` ",
            "<if test='message.parent != null'> ,`parent` ",
            "</if>",
            ")VALUES( #{header.id} , #{message.user.id} , #{message.timeCreate} ",
            "<if test='message.parent != null'> , #{message.parent.id} ",
            "</if>",
            ")",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "message.id")
    Integer insertItem(@Param("header") MessageHeader header, @Param("message") MessageItem message);

    @Insert({"<script>",
            "INSERT INTO `message_tags` (`header`, `tag`) VALUES ",
            "<foreach item='item' collection='tags' separator=','>",
            "( #{header.id},  #{item.tag} )",
            "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "tags.id")
    Integer insertTags(@Param("header") MessageHeader message, @Param("tags") List<MessageTag> messageTags);

    @Insert("INSERT INTO `message_text` (`infoid`, `state`, `body`) VALUES " +
            "( #{message.id}, #{body.state}, #{body.body} )")
    @Options(useGeneratedKeys = true, keyProperty = "body.id")
    Integer insertBody(@Param("message") MessageItem messageItem, @Param("body") MessageBody body);

    @Insert({"INSERT INTO `message_rating` (`infoid`, `userid`, `rating`) " +
            "VALUES ( #{message.id},  #{mRating.user.id},  #{mRating.rating})"})
    @Options(useGeneratedKeys = true, keyProperty = "mRating.id")
    Integer addMessageRatingOnMessageItem(@Param("message") MessageItem message, @Param("mRating") MessageRating messageRating);

    //----------------SELECT-----------------------------------------------
    @Select("SELECT `id` , `priority` , `topic` " +
            "FROM `message_header` " +
            "WHERE `id` = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "root", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getFirstMessageByHeader", fetchType = FetchType.LAZY)),
            @Result(property = "forum", column = "id", javaType = Forum.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getForumByMessageHeader", fetchType = FetchType.LAZY)),
            @Result(property = "messageTags", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getTagsByMessage", fetchType = FetchType.LAZY)),
    })
    MessageHeader getHeaderById(int id);

    @Select({"<script>",
            "SELECT `message_header`.`id` , `message_header`.`priority` , `message_header`.`topic` , `message_info`.`timeCreate` " +
                    "FROM `message_info` " +
                    "INNER JOIN `message_header`  " +
                    "ON (`message_info`.`header` = `message_header`.`id`) " +
                    "WHERE `forumid` = #{forum.id} AND ISNULL(`message_info`.`parent`) " +
                    "ORDER BY `message_header`.`priority` ASC  ",
            "<if test='order == @net.thumbtack.school.forums.model.types.Order@ASC '> , `message_info`.`timeCreate` ASC ",
            "</if>",
            "<if test='order == @net.thumbtack.school.forums.model.types.Order@DESC '> , `message_info`.`timeCreate` DESC ",
            "</if>",
            "<if test=' limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test=' offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "root", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getFirstMessageByHeader", fetchType = FetchType.LAZY)),
            @Result(property = "forum", column = "id", javaType = Forum.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getForumByMessageHeader", fetchType = FetchType.LAZY)),
            @Result(property = "messageTags", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getTagsByMessage", fetchType = FetchType.LAZY)),
    })
    List<MessageHeader> getListByForumWithParams(@Param("forum") Forum forum, @Param("limit") Integer limit, @Param("offset") Integer offset, @Param("order") Order order);

    @Select("SELECT `message_header`.`id` , `message_header`.`priority` , `message_header`.`topic` " +
            "FROM `message_info` " +
            "INNER JOIN `message_header` " +
            "ON (`message_info`.`header` = `message_header`.`id`) " +
            "WHERE `message_info`.`id` = #{message.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "root", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getFirstMessageByHeader", fetchType = FetchType.LAZY)),
            @Result(property = "forum", column = "id", javaType = Forum.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getForumByMessageHeader", fetchType = FetchType.LAZY)),
            @Result(property = "messageTags", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getTagsByMessage", fetchType = FetchType.LAZY)),
    })
    MessageHeader getMessageHeaderByMessageItem(@Param("message") MessageItem message);


    @Select("SELECT `id` , `priority` , `topic` " +
            "FROM `message_header` " +
            "WHERE `forumid` = #{forum.id} " +
            "ORDER BY `priority` ASC ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "root", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getFirstMessageByHeader", fetchType = FetchType.LAZY)),
            @Result(property = "forum", column = "id", javaType = Forum.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getForumByMessageHeader", fetchType = FetchType.LAZY)),
            @Result(property = "messageTags", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getTagsByMessage", fetchType = FetchType.LAZY)),
    })
    List<MessageHeader> getListByForum(@Param("forum") Forum forum);

    @Select("SELECT `id` , `timeCreate` " +
            "FROM `message_info` " +
            "WHERE  `id` = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getUserByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "parent", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageItemParent", fetchType = FetchType.LAZY)),
            @Result(property = "messageBodyList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageBodyByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageRatingList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByRoot", fetchType = FetchType.LAZY)),
            @Result(property = "rating", column = "id", javaType = Double.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAvgRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "rated", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountCommentsByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageHeader", column = "id", javaType = MessageHeader.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageHeaderByMessageItem", fetchType = FetchType.LAZY))
    })
    MessageItem getItemById(int id);

    @Select({"<script>",
            "SELECT `id` , `priority` , `topic` " +
                    "FROM `message_header`",
            "<if test=' limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test=' offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "root", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getFirstMessageByHeader", fetchType = FetchType.LAZY)),
            @Result(property = "forum", column = "id", javaType = Forum.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getForumByMessageHeader", fetchType = FetchType.LAZY)),
            @Result(property = "messageTags", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getTagsByMessage", fetchType = FetchType.LAZY)),
    })
    List<MessageHeader> getAllHeadersWithParams(@Param("limit") Integer limit, @Param("offset") Integer offset);

    @Select("SELECT `id` , `priority` , `topic` " +
            "FROM `message_header`")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "root", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getFirstMessageByHeader", fetchType = FetchType.LAZY)),
            @Result(property = "forum", column = "id", javaType = Forum.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getForumByMessageHeader", fetchType = FetchType.LAZY)),
            @Result(property = "messageTags", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getTagsByMessage", fetchType = FetchType.LAZY)),
    })
    List<MessageHeader> getAllHeaders();

    @Select("SELECT `id` , `timeCreate` " +
            "FROM `message_info`")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getUserByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "parent", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageItemParent", fetchType = FetchType.LAZY)),
            @Result(property = "messageBodyList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageBodyByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageRatingList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByRoot", fetchType = FetchType.LAZY)),
            @Result(property = "rating", column = "id", javaType = Double.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAvgRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "rated", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountCommentsByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageHeader", column = "id", javaType = MessageHeader.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageHeaderByMessageItem", fetchType = FetchType.LAZY))
    })
    List<MessageItem> getAllMessageItems();

    @Select("SELECT `message_info`.`id` , `message_info`.`timeCreate` " +
            "FROM `message_info` " +
            "INNER JOIN `message_header` " +
            "ON (`message_info`.`header` = `message_header`.`id`) " +
            "WHERE `forumid` = #{forum.id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getUserByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "parent", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageItemParent", fetchType = FetchType.LAZY)),
            @Result(property = "messageBodyList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageBodyByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageRatingList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByRoot", fetchType = FetchType.LAZY)),
            @Result(property = "rating", column = "id", javaType = Double.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAvgRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "rated", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountCommentsByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageHeader", column = "id", javaType = MessageHeader.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageHeaderByMessageItem", fetchType = FetchType.LAZY))
    })
    List<MessageItem> getAllMessageItemsByForum(@Param("forum") Forum forum);

    @Select({"<script>",
            "SELECT `id` , `timeCreate` " +
                    "FROM `message_info` ",
            "<if test=' limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test=' offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getUserByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "parent", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageItemParent", fetchType = FetchType.LAZY)),
            @Result(property = "messageBodyList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageBodyByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageRatingList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByRoot", fetchType = FetchType.LAZY)),
            @Result(property = "rating", column = "id", javaType = Double.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAvgRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "rated", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountCommentsByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageHeader", column = "id", javaType = MessageHeader.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageHeaderByMessageItem", fetchType = FetchType.LAZY))
    })
    List<MessageItem> getAllMessageItemsWithParams(@Param("limit") Integer limit, @Param("offset") Integer offset);

    @Select("SELECT `id` , `timeCreate` " +
            "FROM `message_info` " +
            "WHERE  `header` = #{header.id} AND isnull(`parent`) ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getUserByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "parent", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageItemParent", fetchType = FetchType.LAZY)),
            @Result(property = "messageBodyList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageBodyByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageRatingList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByRoot", fetchType = FetchType.LAZY)),
            @Result(property = "rating", column = "id", javaType = Double.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAvgRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "rated", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountCommentsByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageHeader", column = "id", javaType = MessageHeader.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageHeaderByMessageItem", fetchType = FetchType.LAZY))
    })
    MessageItem getFirstMessageByHeader(@Param("header") MessageHeader header);

    @Select("SELECT `id` , `timeCreate` " +
            "FROM `message_info` " +
            "WHERE `parent` = #{message.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getUserByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "parent", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageItemParent", fetchType = FetchType.LAZY)),
            @Result(property = "messageBodyList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageBodyByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageRatingList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByRoot", fetchType = FetchType.LAZY)),
            @Result(property = "rating", column = "id", javaType = Double.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAvgRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "rated", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountCommentsByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageHeader", column = "id", javaType = MessageHeader.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageHeaderByMessageItem", fetchType = FetchType.LAZY))
    })
    List<MessageItem> getListMessageItemByRoot(@Param("message") MessageItem message);

    @Select({"<script>",
            "SELECT `id` , `timeCreate` " +
                    "FROM `message_info` " +
                    "WHERE `parent` = #{message.id}",
            "<if test=' order == @net.thumbtack.school.forums.model.types.Order@ASC'> ORDER BY `timeCreate` ASC ",
            "</if>",
            "<if test=' order == @net.thumbtack.school.forums.model.types.Order@DESC'> ORDER BY `timeCreate` DESC ",
            "</if>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getUserByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "parent", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageItemParent", fetchType = FetchType.LAZY)),
            @Result(property = "messageBodyList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageBodyByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageRatingList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByRoot", fetchType = FetchType.LAZY)),
            @Result(property = "rating", column = "id", javaType = Double.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAvgRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "rated", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountCommentsByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageHeader", column = "id", javaType = MessageHeader.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageHeaderByMessageItem", fetchType = FetchType.LAZY))
    })
    List<MessageItem> getListMessageItemByRootWithParam(@Param("message") MessageItem message, @Param("order") Order order);

    @Select("SELECT `parent`.`id`, `parent`.`timeCreate` " +
            "FROM `message_info` AS `child` " +
            "INNER JOIN `message_info` AS `parent`  " +
            "ON (`child`.`parent` = `parent`.`id`) " +
            "WHERE `child`.`id` = #{message.id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getUserByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "parent", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageItemParent", fetchType = FetchType.LAZY)),
            @Result(property = "messageBodyList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageBodyByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageRatingList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByRoot", fetchType = FetchType.LAZY)),
            @Result(property = "rating", column = "id", javaType = Double.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAvgRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "rated", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountCommentsByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageHeader", column = "id", javaType = MessageHeader.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageHeaderByMessageItem", fetchType = FetchType.LAZY))
    })
    MessageItem getMessageItemParent(@Param("message") MessageItem message);


    @Select("SELECT `message_text`.`id` , `message_text`.`state` , `message_text`.`body` " +
            "FROM `message_text` " +
            "INNER JOIN `message_info` " +
            "ON (`message_text`.`infoid` = `message_info`.`id`) " +
            "WHERE `message_info`.`id` = #{message.id} " +
            "ORDER BY `message_text`.`state` DESC, `message_text`.`id` DESC ")
    List<MessageBody> getListMessageBodyByMessageItem(@Param("message") MessageItem message);

    @Select("SELECT `message_rating`.`id`, `message_rating`.`rating` " +
            "FROM `message_rating` " +
            "INNER JOIN `message_info` " +
            "ON (`message_rating`.`infoid` = `message_info`.`id`) " +
            "WHERE `message_info`.`id` = #{message.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getUserByMessageRating", fetchType = FetchType.LAZY)),
    })
    List<MessageRating> getListMessageRatingByMessageItem(@Param("message") MessageItem message);

    @Select("SELECT `id` , `timeCreate` " +
            "FROM `message_info` " +
            "WHERE `userid`= #{user.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getUserByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "parent", column = "id", javaType = MessageItem.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageItemParent", fetchType = FetchType.LAZY)),
            @Result(property = "messageBodyList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageBodyByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageRatingList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByRoot", fetchType = FetchType.LAZY)),
            @Result(property = "rating", column = "id", javaType = Double.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAvgRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "rated", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountRatingByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getCountCommentsByMessageItem", fetchType = FetchType.LAZY)),
            @Result(property = "messageHeader", column = "id", javaType = MessageHeader.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getMessageHeaderByMessageItem", fetchType = FetchType.LAZY))
    })
    List<MessageItem> getListMessageItemByUser(@Param("user") User user);


    @Select("SELECT IFNULL(AVG(`message_rating`.`rating`),0) " +
            "FROM `message_rating` " +
            "INNER JOIN `message_info`  " +
            "ON (`message_rating`.`infoid` = `message_info`.`id`) " +
            "WHERE `message_info`.`id` = #{message.id}")
    Double getAvgRatingByMessageItem(@Param("message") MessageItem message);

    @Select("SELECT COUNT(`message_info`.`parent`) " +
            "FROM `message_info` " +
            "INNER JOIN `message_info` AS `parent` " +
            "ON (`message_info`.`parent` = `parent`.`id`) " +
            "WHERE `parent`.`id` = #{message.id} ")
    Integer getCountCommentsByMessageItem(@Param("message") MessageItem message);

    @Select("SELECT COUNT(`rating`) AS `rated` " +
            "FROM `message_rating` " +
            "WHERE `infoid` = #{message.id} ")
    Integer getCountRatingByMessageItem(@Param("message") MessageItem message);

    @Select("SELECT `id` , `tag` FROM `message_tags` " +
            "WHERE `header` = #{header.id} " +
            "ORDER BY `tag` DESC")
    List<MessageTag> getTagsByMessage(@Param("header") MessageHeader message);

    @Select({"<script>",
            "SELECT `id` , `tag` FROM `message_tags` " +
                    "WHERE `header` = #{header.id} ",
            "<if test='order == @net.thumbtack.school.forums.model.types.Order@ASC'> ORDER BY `id` ASC",
            "</if>",
            "<if test='order == @net.thumbtack.school.forums.model.types.Order@DESC'> ORDER BY `id` DESC",
            "</if>",
            "</script>"})
    List<MessageTag> getTagsByMessageWithParam(@Param("header") MessageHeader message, @Param("order") Order order);

    @Select("SELECT EXISTS(SELECT `rating` " +
            "FROM `message_rating` " +
            "WHERE `infoid` = #{message.id} AND `userid` = #{user.id})")
    boolean checkRatingUserOnMessage(@Param("message") MessageItem message, @Param("user") User user);

    @Select("SELECT EXISTS(SELECT `id`, `timeCreate` " +
            "FROM `forums`.`message_info` " +
            "WHERE `id` = #{id})")
    boolean checkMessageItemById(int id);


    //---------------------------UPDATE---------------------------------------------------------
    @Update("UPDATE `message_text` " +
            "SET `state` =  #{state}" +
            "WHERE `infoid` = #{message.id}")
    void updateMessageStateOnAllBody(@Param("message") MessageItem message, @Param("state") MessageState state);

    @Update("UPDATE `message_header` " +
            "SET `forumid` = #{header.forum.id}, `priority` = #{header.priority}, `topic` = #{header.topic} " +
            "WHERE `id` = #{header.id}")
    void updateHeader(@Param("header") MessageHeader message);

    @Update("UPDATE `message_info` " +
            "SET `parent` = null, `header` = #{header.id} " +
            "WHERE `id` = #{message.id}")
    void conversionCommentIntoMessage(@Param("header") MessageHeader header, @Param("message") MessageItem message);

    @Update({"<script>",
            "UPDATE `message_info` SET ",
            "<if test='message.parent != null'> `parent` = #{message.parent.id},",
            "</if>",
            " `userid` = #{message.user.id}, `timeCreate` = #{message.timeCreate} " +
                    "WHERE `id` = #{message.id}",
            "</script>"})
    void updateItem(@Param("message") MessageItem message);

    @Update("UPDATE `message_text` " +
            "SET `state` = #{body.state}, `body` = #{body.body} " +
            "WHERE `id` = #{body.id} ")
    void updateBody(@Param("body") MessageBody body);

    @Update({"UPDATE `message_rating` " +
            "SET `rating` = #{rating} " +
            "WHERE `infoid` = #{message.id} AND `userid` = #{user.id}"})
    void updateRatingOnMessage(@Param("message") MessageItem message, @Param("user") User user, @Param("rating") int rating);


    //-------------------DELETE-----------------------------------------------------------
    @Delete("DELETE FROM `message_header` " +
            "WHERE `id` = #{header.id}")
    int deleteMessageHeader(@Param("header") MessageHeader header);

    @Delete("DELETE FROM `message_info` " +
            "WHERE `id` = #{message.id}")
    int deleteMessageItem(@Param("message") MessageItem message);

    @Delete("DELETE FROM `message_text` " +
            "WHERE `id` = #{body.id}")
    int deleteMessageBody(@Param("body") MessageBody body);


    @Delete("DELETE FROM `message_tags` " +
            "WHERE `header` =  #{header.id}")
    int deleteTagsOnMessageHeader(@Param("header") MessageHeader header);

    @Delete("DELETE FROM `message_rating` " +
            "WHERE `infoid` = #{message.id} AND `userid` = #{user.id}")
    int delRatingOnMessage(@Param("message") MessageItem message, @Param("user") User user);


}
