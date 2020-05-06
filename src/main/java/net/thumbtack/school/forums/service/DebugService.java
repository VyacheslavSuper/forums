package net.thumbtack.school.forums.service;

import net.thumbtack.school.forums.dao.CommonDao;
import net.thumbtack.school.forums.dto.request.RequestBase;
import net.thumbtack.school.forums.dto.response.ResponseBase;
import net.thumbtack.school.forums.exception.ForumException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DebugService {

    @Autowired
    private CommonDao commonDao;


    public ResponseBase clear(RequestBase request) throws ForumException {
        commonDao.clear();
        return new ResponseBase();
    }
}
