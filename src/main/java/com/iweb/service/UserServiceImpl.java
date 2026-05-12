package com.iweb.service;

import com.iweb.dao.UserDao;
import com.iweb.dto.DataGrid;
import com.iweb.dto.PageDto;
import com.iweb.model.Scheduled;
import com.iweb.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User findUserByAccount(String account) throws Exception {
        return userDao.selectByAccount(account);
    }

    @Override
    public void findUserList(DataGrid<User> dataGrid, PageDto dto) throws Exception {
        // 查询总数
        int total = userDao.selectCount();
        // 分页查询
        List<User> users = userDao.selectList(dto);
        dataGrid.setTotal(total);
        dataGrid.setData(users);
    }

    @Override
    public void modifyById(User user) throws Exception {
        userDao.updateById(user);
    }

    @Override
    public void saveUser(User user, Scheduled scheduled) throws Exception {
        userDao.save(user,scheduled);
    }

    @Override
    public List<User> findUserDepartment() throws Exception {
        return userDao.selectUserDepartment();
    }

    @Override
    public List<User> findUserRole() throws Exception {
        return userDao.selectRole();
    }

    @Override
    public void modityPwd(User user) throws Exception {
        userDao.updatePwd(user);
    }
}
