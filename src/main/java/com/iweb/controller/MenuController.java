package com.iweb.controller;

import cn.hutool.core.convert.Convert;
import com.iweb.model.Menu;
import com.iweb.service.MenuService;

import javax.servlet.http.HttpSession;
import java.util.List;

public class MenuController {

    private MenuService menuService;

    public void setMenuService(MenuService menuService) {
        this.menuService = menuService;
    }


    public List<Menu> menuList(HttpSession session) throws Exception {
        String roleName = Convert.toStr(session.getAttribute("roleName"));
        return menuService.listRole(roleName);
    }

}
