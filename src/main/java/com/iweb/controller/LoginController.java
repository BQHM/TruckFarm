package com.iweb.controller;

import com.iweb.dto.Result;
import com.iweb.model.User;
import com.iweb.service.UserService;

import javax.servlet.http.HttpSession;

public class LoginController {

    private UserService userService;

    public void setUserService(UserService userService){
        this.userService = userService;
    }
    public Result login(User user, HttpSession session) throws Exception{
        User data = userService.findUserByAccount(user.getAccount());
        if(data==null){
            throw new RuntimeException("用户不存在");
        }if (!user.getPassword().equals(data.getPassword())){
            throw new RuntimeException("密码错误");
        }
        session.setAttribute("account",data.getAccount());
        session.setAttribute("password",data.getPassword());
        session.setAttribute("roleName",data.getRoleName());
        return new Result(true,"login");
    }

}
