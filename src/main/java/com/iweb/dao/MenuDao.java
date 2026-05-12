package com.iweb.dao;

import com.iweb.model.Menu;

import java.util.List;

public interface MenuDao {
    List<Menu> selectRoot()throws Exception;

    List<Menu> selectChildren(String pid)throws Exception;
}
