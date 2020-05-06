package net.thumbtack.school.forums.dao.mappers;

import net.thumbtack.school.forums.model.Forum;
import net.thumbtack.school.forums.model.Session;
import net.thumbtack.school.forums.model.User;
import net.thumbtack.school.forums.model.message.MessageItem;
import net.thumbtack.school.forums.model.message.MessageRating;
import net.thumbtack.school.forums.model.types.RestrictionType;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO `user` ( `login`, `password`, `name`, `email`, `userType`, `timeRegistered`, `restrictionType`,`banCount`,`banTime` ) " +
            "VALUES ( #{user.login}, #{user.password}, #{user.name}, #{user.email}, #{user.userType}, #{user.timeRegistered}, #{user.restrictionType}, #{user.banCount}, #{user.banTime} )")
    @Options(useGeneratedKeys = true, keyProperty = "user.id")
    Integer insert(@Param("user") User user);

    @Select("SELECT * " +
            "FROM `user` " +
            "WHERE `id` = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByUser", fetchType = FetchType.LAZY)),
            @Result(property = "forums", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getByUser", fetchType = FetchType.LAZY))
    })
    User getById(int id);

    @Select("SELECT EXISTS(SELECT * " +
            "FROM `user` " +
            "WHERE `id` = #{id})")
    boolean checkUserById(int id);

    @Select("SELECT EXISTS(SELECT * " +
            "FROM `user` " +
            "WHERE `login` = #{login})")
    boolean checkUserByLogin(String login);

    @Select("SELECT `user`.* " +
            "FROM `forum` " +
            "INNER JOIN `user` " +
            "ON (`forum`.`userid` = `user`.`id`) " +
            "WHERE `forum`.`id` = #{forum.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByUser", fetchType = FetchType.LAZY)),
            @Result(property = "forums", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getByUser", fetchType = FetchType.LAZY))
    })
    User getByForum(@Param("forum") Forum forum);

    @Select("SELECT * " +
            "FROM `user` " +
            "WHERE `login` = #{login} AND `deleted` = FALSE")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByUser", fetchType = FetchType.LAZY)),
            @Result(property = "forums", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getByUser", fetchType = FetchType.LAZY))
    })
    User getByLogin(String login);

    @Select("SELECT `user`.* " +
            "FROM `session` INNER JOIN `user` " +
            "ON (`session`.`userid` = `user`.`id`) " +
            "WHERE `session`.`id` = #{session.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByUser", fetchType = FetchType.LAZY)),
            @Result(property = "forums", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getByUser", fetchType = FetchType.LAZY))
    })
    User getBySessionCookie(@Param("session") Session session);

    @Select({"<script>",
            "SELECT * " +
                    "FROM `user`",
            "<if test='limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test='offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByUser", fetchType = FetchType.LAZY)),
            @Result(property = "forums", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getByUser", fetchType = FetchType.LAZY))
    })
    List<User> getAll(@Param("limit") Integer limit, @Param("offset") Integer offset);

    @Select({"<script>",
            "SELECT * " +
            "FROM `user` " +
                    "WHERE `restrictionType` = #{restrictionType}",
            "<if test='limit != null'> LIMIT #{limit} ",
            "</if>",
            "<if test='offset != null'> OFFSET #{offset} ",
            "</if>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByUser", fetchType = FetchType.LAZY)),
            @Result(property = "forums", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getByUser", fetchType = FetchType.LAZY))
    })
    List<User> getUsersByRestrictionType(RestrictionType restrictionType, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Select("SELECT `user`.* " +
            "FROM `message_rating` " +
            "INNER JOIN `user`  " +
            "ON (`message_rating`.`userid` = `user`.`id`) " +
            "WHERE `message_rating`.`id`= #{messageRating.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByUser", fetchType = FetchType.LAZY)),
            @Result(property = "forums", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getByUser", fetchType = FetchType.LAZY))
    })
    User getUserByMessageRating(@Param("messageRating") MessageRating messageRating);

    @Select("SELECT `user`.* " +
            "FROM `message_info` " +
            "INNER JOIN `user` " +
            "ON (`message_info`.`userid` = `user`.`id`) " +
            "WHERE `message_info`.`id` = #{message.id} ")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "messages", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.MessageMapper.getListMessageItemByUser", fetchType = FetchType.LAZY)),
            @Result(property = "forums", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.forums.dao.mappers.ForumMapper.getByUser", fetchType = FetchType.LAZY))
    })
    User getUserByMessageItem(@Param("message") MessageItem message);

    @Update("UPDATE `user` " +
            "SET " +
            "`login` = #{user.login}, `password` = #{user.password}, `name` = #{user.name}, " +
            "`email` = #{user.email}, `userType` = #{user.userType}, `timeRegistered` = #{user.timeRegistered}, " +
            "`restrictionType` = #{user.restrictionType}, `banCount` = #{user.banCount}, `banTime` = #{user.banTime} " +
            "WHERE `id` = #{user.id}")
    void update(@Param("user") User user);

    @Update("UPDATE `user` " +
            "SET `restrictionType` = 'FULL', `banTime` = NULL " +
            "WHERE `restrictionType`='LIM' AND `banTime` <= #{time} ")
    void unBanUsers(LocalDate time);

    @Update("UPDATE `user` " +
            "SET " +
            "`deleted` = true " +
            "WHERE `id` = #{user.id}")
    void safeDelete(@Param("user") User user);

    @Delete("DELETE FROM `user` " +
            "WHERE `login` = #{user.login}")
    int delete(@Param("user") User user);

}
