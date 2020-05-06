package net.thumbtack.school.forums.dao.mappers;

import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.message.MessageHeader;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ForumMapper {
    @Insert("INSERT INTO `forum` (`userid`, `topic`, `forumType`) VALUES " +
            "( #{forum.user.id}, #{forum.topic}, #{forum.forumType})")
    @Options(useGeneratedKeys = true, keyProperty = "forum.id")
    Integer insert(@Param("forum") Forum forum);

    @Select("SELECT `id`, `topic`, `forumType`, `readonly` " +
            "FROM `forum` " +
            "WHERE `id` = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getByForum", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListByForum", fetchType = FetchType.LAZY)),
            @Result(property = "allMessageItems", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAllMessageItemsByForum", fetchType = FetchType.LAZY)),
            @Result(property = "countMessages", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountMessageOnForum", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountCommentsOnForum", fetchType = FetchType.LAZY)),
    })
    Forum getById(int id);

    @Select("SELECT EXISTS(SELECT `id`, `topic`, `forumType`, `readonly` " +
            "FROM `forum` " +
            "WHERE `id` = #{id})")
    boolean checkForumById(int id);

    @Select("SELECT `id`, `topic`, `forumType` " +
            "FROM `forum` ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getByForum", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListByForum", fetchType = FetchType.LAZY)),
            @Result(property = "allMessageItems", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAllMessageItemsByForum", fetchType = FetchType.LAZY)),
            @Result(property = "countMessages", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountMessageOnForum", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountCommentsOnForum", fetchType = FetchType.LAZY)),
    })
    List<Forum> getAll();

    @Select({"<script>",
            "SELECT `id`, `topic`, `forumType` " +
                    "FROM `forum` ",
            "<if test='limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test='offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getByForum", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListByForum", fetchType = FetchType.LAZY)),
            @Result(property = "allMessageItems", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAllMessageItemsByForum", fetchType = FetchType.LAZY)),
            @Result(property = "countMessages", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountMessageOnForum", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountCommentsOnForum", fetchType = FetchType.LAZY)),
    })
    List<Forum> getAllWithParams(@Param("limit") Integer limit, @Param("offset") Integer offset);

    @Select("SELECT `forum`.`id`,`forum`.`topic`,`forum`.`forumType`,`forum`.`readonly` " +
            "FROM `message_header` " +
            "INNER JOIN `forum` " +
            "ON (`message_header`.`forumid` = `forum`.`id`) " +
            "WHERE `message_header`.`id` = #{header.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getByForum", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListByForum", fetchType = FetchType.LAZY)),
            @Result(property = "allMessageItems", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAllMessageItemsByForum", fetchType = FetchType.LAZY)),
            @Result(property = "countMessages", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountMessageOnForum", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountCommentsOnForum", fetchType = FetchType.LAZY)),
    })
    Forum getForumByMessageHeader(@Param("header") MessageHeader message);

    @Select("SELECT IFNULL(COUNT(`id`),0) AS `countMessages` " +
            "FROM `message_header` " +
            "WHERE `forumid`= #{forum.id}")
    int getCountMessageOnForum(@Param("forum") Forum forum);

    @Select("SELECT IFNULL(COUNT(`message_info`.`parent`),0) AS `countComments` " +
            "FROM `message_info` " +
            "INNER JOIN `message_header` " +
            "ON (`message_info`.`header` = `message_header`.`id`) " +
            "WHERE `forumid`= #{forum.id}")
    int getCountCommentsOnForum(@Param("forum") Forum forum);

    @Update("UPDATE `forum` " +
            "SET `topic` = #{forum.topic}, `forumType` = #{forum.forumType} " +
            "WHERE `id` = #{forum.id}")
    void update(@Param("forum") Forum forum);

    @Select("SELECT `id`, `topic`, `forumType`, `readonly` " +
            "FROM `forum` " +
            "WHERE `userid` = #{user.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getByForum", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListByForum", fetchType = FetchType.LAZY)),
            @Result(property = "allMessageItems", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAllMessageItemsByForum", fetchType = FetchType.LAZY)),
            @Result(property = "countMessages", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountMessageOnForum", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountCommentsOnForum", fetchType = FetchType.LAZY)),
    })
    List<Forum> getByUser(@Param("user") User user);

    //need test
    @Select({"<script>",
            "SELECT `id`, `topic`, `forumType`, `readonly` " +
                    "FROM `forum` " +
                    "WHERE `userid` = #{user.id}",
            "<if test='limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test='offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getByForum", fetchType = FetchType.LAZY)),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListByForum", fetchType = FetchType.LAZY)),
            @Result(property = "allMessageItems", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getAllMessageItemsByForum", fetchType = FetchType.LAZY)),
            @Result(property = "countMessages", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountMessageOnForum", fetchType = FetchType.LAZY)),
            @Result(property = "countComments", column = "id", javaType = Integer.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getCountCommentsOnForum", fetchType = FetchType.LAZY)),
    })
    List<Forum> getByUserWithParams(@Param("user") User user, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Update("UPDATE `forum` " +
            "SET `readonly` = true " +
            "WHERE `id` = #{forum.id}")
    void safeDelete(@Param("forum") Forum forum);

    @Delete("DELETE FROM `forum` " +
            "WHERE `id` = #{forum.id}")
    int delete(@Param("forum") Forum forum);

}
