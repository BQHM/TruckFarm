package com.iweb.dao;

import com.iweb.dto.PageDto;
import com.iweb.model.Scheduled;
import com.iweb.model.User;

import java.util.List;

public interface UserDao {
    List<User> selectList(PageDto dto) throws Exception;

    User selectByAccount(String account) throws Exception;

    int selectCount() throws Exception;

    void updateById(User user) throws Exception;

    void save(User user, Scheduled scheduled) throws Exception;

    List<User> selectUserDepartment() throws Exception;

    List<User> selectRole() throws Exception;

    void updatePwd(User user)throws Exception;
}
