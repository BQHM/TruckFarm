package com.iweb.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Menu {

    private String id;
    private String iconCls;

    private String text;

    private String url;

    private String pid;

    private String roleName;

    private List<Menu> children;

    public Menu(String id, String iconCls, String text, String url, String pid, String roleName) {
        this.id = id;
        this.iconCls = iconCls;
        this.text = text;
        this.url = url;
        this.pid = pid;
        this.roleName = roleName;
    }

}
