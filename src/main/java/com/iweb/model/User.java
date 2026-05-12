package com.iweb.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String userId;
    private String account;
    private String password;
    private String userName;
    private String phone;
    private Date createTime;
    private String departmentName;
    private String dimission;
    private String roleName;

    public User(String userId, String account, String password, String userName, String phone, Date createTime, String dimission, String roleName) {
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.userName = userName;
        this.phone = phone;
        this.createTime = createTime;
        this.dimission = dimission;
        this.roleName = roleName;
    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public User(String account, String userName, String phone, String departmentName, String dimission,String roleName) {
        this.account = account;
        this.userName = userName;
        this.phone = phone;
        this.departmentName = departmentName;
        this.dimission = dimission;
        this.roleName=roleName;
    }

    public User(String roleName,String departmentName,String dimission) {
        this.roleName = roleName;
        this.departmentName=departmentName;
        this.dimission=dimission;
    }



}
