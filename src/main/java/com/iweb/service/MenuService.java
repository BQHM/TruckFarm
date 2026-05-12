package com.iweb.service;

import com.iweb.model.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> listRole(String roleName)throws Exception;
}
