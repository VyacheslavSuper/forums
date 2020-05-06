package net.thumbtack.school.forums.dao.mappers;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DebugMapper {

    @Delete("DELETE FROM `forums`.`user`")
    void deleteUserAll();

    @Delete("DELETE FROM `forums`.`forum`")
    void deleteForumAll();

    @Delete("DELETE FROM `forums`.`session`")
    void deleteSessionsAll();

    @Delete("DELETE FROM `forums`.`message_header`")
    void deleteMessageAll();
}
