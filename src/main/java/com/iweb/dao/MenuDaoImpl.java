package com.iweb.dao;

import com.iweb.model.Menu;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuDaoImpl implements MenuDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Menu> selectRoot() throws Exception {
        List<Menu> menus = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        try {

            String sql = "select * from tb_menu where pid is null";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Menu menu = new Menu(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6)
                );
                menus.add(menu);
            }

        } finally {
            conn.close();
        }
        return menus;
    }

    @Override
    public List<Menu> selectChildren(String pid) throws Exception {
        List<Menu> menus = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        try {
            String sql = "SELECT * FROM TB_MENU WHERE PID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, pid);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Menu menu = new Menu(rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6));
                menus.add(menu);
            }
        } finally {
            conn.close();
        }
        return menus;
    }
}
