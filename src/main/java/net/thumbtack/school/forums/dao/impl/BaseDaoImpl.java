package net.thumbtack.school.forums.dao.impl;

import net.thumbtack.school.forums.dao.mappers.*;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDaoImpl {
    @Autowired
    protected ForumMapper forumMapper;
    @Autowired
    protected UserMapper userMapper;
    @Autowired
    protected SessionMapper sessionMapper;
    @Autowired
    protected ResponseMapper responseMapper;
    @Autowired
    protected MessageMapper messageMapper;
    @Autowired
    protected DebugMapper debugMapper;
}
