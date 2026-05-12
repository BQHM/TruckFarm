package com.iweb.service;

import com.iweb.dao.MenuDao;
import com.iweb.model.Menu;

import java.util.List;

public class MenuServiceImpl implements MenuService {

    private MenuDao menuDao;

    public void setMenuDao(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    @Override
    public List<Menu> listRole(String roleName) throws Exception {
        // 一级菜单
        List<Menu> root = menuDao.selectRoot();
        findChildren(root, roleName);
        return root;
    }

    public void findChildren(List<Menu> root, String roleName) throws Exception {
        for (int i = root.size() - 1; i >= 0; i--) {
            Menu menu = root.get(i);
            if (!menu.getRoleName().contains(roleName)) {
                root.remove(i);
            }
        }
        for (Menu menu : root) {
            String pid = menu.getId();
            List<Menu> children = menuDao.selectChildren(pid);
            if (children.size() > 0) {
                menu.setChildren(children);
            }
            findChildren(children, roleName);
        }
    }
}
