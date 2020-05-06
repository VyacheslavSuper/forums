package net.thumbtack.school.forums.dao.mappers;

import net.thumbtack.school.forums.model.Session;
import net.thumbtack.school.forums.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
@Mapper
public interface SessionMapper {

    @Insert("INSERT INTO `session` ( `cookie` , `userid` ) VALUES " +
            "( #{session.cookie}, #{session.user.id} )")
    @Options(useGeneratedKeys = true, keyProperty = "session.id")
    Integer insert(@Param("session") Session session);

    @Select("SELECT `id`,`cookie` " +
            "FROM `session` " +
            "WHERE `id` = #{id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getBySessionCookie", fetchType = FetchType.LAZY))
    })
    Session getById(int id);

    @Select("SELECT EXISTS(SELECT `id`,`cookie` " +
            "FROM `session` " +
            "WHERE `id` = #{id})")
    boolean checkSessionById(int id);

    @Select("SELECT EXISTS(SELECT `id`,`cookie` " +
            "FROM `session` " +
            "WHERE `cookie` = #{cookie})")
    boolean checkSessionByCookie(String cookie);

    @Select("SELECT EXISTS(SELECT `session`.`id`,`cookie` " +
            "FROM `session` " +
            "INNER JOIN `user`  " +
            "ON (`session`.`userid` = `user`.`id`) " +
            "WHERE `user`.`login`= #{login})")
    boolean checkSessionByUserLogin(String login);

    @Select("SELECT `session`.`id`,`cookie` " +
            "FROM `session` " +
            "INNER JOIN `user` " +
            "ON (`session`.`userid` = `user`.`id`) " +
            "WHERE `user`.`login`= #{login}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getBySessionCookie", fetchType = FetchType.LAZY))
    })
    Session getByUserLogin(String login);

    @Select("SELECT `id`,`cookie` " +
            "FROM `session` " +
            "WHERE `cookie` = #{cookie}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "user", column = "id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.forums.dao.mappers.UserMapper.getBySessionCookie", fetchType = FetchType.LAZY))
    })
    Session getByCookie(String cookie);

    @Delete("DELETE FROM `session` WHERE `cookie` = #{session.cookie}")
    int delete(@Param("session") Session session);

    @Delete("DELETE FROM `session` WHERE `cookie` = #{cookie}")
    int deleteByCookie(String cookie);

    @Delete("DELETE FROM `session` WHERE `userid` = #{user.id}")
    int deleteByUser(@Param("user") User user);

}
