package com.iweb.controller;

import cn.hutool.core.lang.UUID;
import com.iweb.dto.DataGrid;
import com.iweb.dto.PageDto;
import com.iweb.dto.Result;
import com.iweb.model.Scheduled;
import com.iweb.model.User;
import com.iweb.service.UserService;

import java.util.Date;
import java.util.List;

public class UserController {

    private UserService userService;

    public Result modifyById(User user) throws Exception{
        userService.modifyById(user);
        return new Result(true, "modify User");
    }

    public void setUserService(UserService userService){
        this.userService = userService;
    }

    public DataGrid<User> list(PageDto dto) throws Exception {
        DataGrid<User> dataGrid = new DataGrid<>();
        userService.findUserList(dataGrid, dto);
        return dataGrid;
    }

    public Result saveUser(User user) throws Exception{
        // 处理 员工编码
        user.setUserId(UUID.fastUUID().toString());
        user.setCreateTime(new Date());
        user.setDimission("1");

        Scheduled scheduled =new Scheduled();
        scheduled.setUserId(user.getUserId());
        scheduled.setScheduledId(UUID.fastUUID().toString());

        userService.saveUser(user,scheduled);
        return new Result(true,"save user");
    }

    public List<User> userDepartment() throws Exception{
        return userService.findUserDepartment();
    }

    public List<User> userRole() throws Exception{
        return userService.findUserRole();
    }

    public Result modityPwd(User user) throws Exception{

        userService.modityPwd(user);
        return new Result(true,"change pwd");

    }
}
