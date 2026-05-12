package com.iweb.service;

import com.iweb.dto.DataGrid;
import com.iweb.dto.PageDto;
import com.iweb.model.Scheduled;
import com.iweb.model.User;

import java.util.List;

public interface UserService {
    User findUserByAccount(String account) throws Exception;

    void findUserList(DataGrid<User> dataGrid, PageDto dto) throws Exception;

    void modifyById(User user) throws Exception;

    void saveUser(User user, Scheduled scheduled) throws Exception;

    List<User> findUserDepartment() throws Exception;

    List<User> findUserRole()throws Exception;

    void modityPwd(User user)throws Exception;
}
