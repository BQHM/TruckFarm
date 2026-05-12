package com.iweb.dao;

import com.iweb.dto.PageDto;
import com.iweb.model.Scheduled;
import com.iweb.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> selectList(PageDto dto) throws Exception {
        List<User> users = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        try {
            String field = dto.getFiled();
            String order = dto.getOrder();
            String sql = "select tu.account,tu.name,tu.phone,td.name,tu.dimission,tu.role_name from tb_user tu join tb_scheduled ts on tu.user_id = ts.user_id join tb_department td on ts.department_id=td.department_id order by " + field + " " + order + "  limit ?,?";
            int index = (dto.getIndex() - 1) * dto.getSize();
            int size = dto.getSize();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, index);
            pst.setInt(2, size);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                users.add(user);
            }
        } finally {
            conn.close();
        }
        return users;
    }

    @Override
    public User selectByAccount(String account) throws Exception {
        Connection conn = dataSource.getConnection();
        User user = null;
        try {
            String sql = "select * from tb_user where account = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, account);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                user = new User(
                        rs.getString(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getDate(6),
                        rs.getString(7),
                        rs.getString(8)
                );
            }
        } finally {
            conn.close();
        }
        return user;
    }

    @Override
    public int selectCount() throws Exception {
        Connection conn = dataSource.getConnection();
        int count = 0;
        try {
            String sql = "select count(user_id) from tb_user";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
        } finally {
            conn.close();
        }
        return count;
    }

    @Override
    public void updateById(User user) throws Exception {
        Connection conn = dataSource.getConnection();

        try {
            String sql = "update tb_user set phone = ? where account = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setObject(1, user.getPhone());
            pst.setObject(2, user.getAccount());
            pst.executeUpdate();

        } finally {
            conn.close();
        }

    }

    @Override
    public void save(User user, Scheduled scheduled) throws Exception {
        Connection conn = dataSource.getConnection();

        try {

            String sql1 = "insert into tb_user values(?,?,?,?,?,?,?,?)";
            PreparedStatement pst1 = conn.prepareStatement(sql1);
            pst1.setObject(1, user.getUserId());
            pst1.setObject(2, user.getAccount());
            pst1.setObject(3, user.getPassword());
            pst1.setObject(4, user.getUserName());
            pst1.setObject(5, user.getPhone());
            pst1.setObject(6, new Timestamp(user.getCreateTime().getTime()));
            pst1.setObject(7, user.getDimission());
            pst1.setObject(8, user.getRoleName());
            pst1.executeUpdate();
            String sql2 = "insert into tb_scheduled values (?,?," + "(" + "select department_id from tb_department where name = ?" + ")" + " ,'')";
            PreparedStatement pst2 = conn.prepareStatement(sql2);
            pst2.setObject(1, scheduled.getScheduledId());
            pst2.setObject(2, scheduled.getUserId());
            pst2.setObject(3, user.getDepartmentName());
            pst2.executeUpdate();
            String sql3 = "update tb_department set count=count+1 where name=?";
            PreparedStatement pst3 = conn.prepareStatement(sql3);
            pst3.setObject(1, user.getDepartmentName());
            pst3.executeUpdate();

        } finally {
            conn.close();
        }
    }


    @Override
    public List<User> selectUserDepartment() throws Exception {
        List<User> users = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        try {
            String sql = "select name from  tb_department ";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User user = new User(null, rs.getString(1), null);
                users.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            conn.close();
        }
        return users;
    }

    @Override
    public List<User> selectRole() throws Exception {
        List<User> users = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        try {
            String sql = "select distinct role_name from tb_user";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                User user = new User(rs.getString(1), null, null);
                users.add(user);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            conn.close();
        }
        return users;
    }

    @Override
    public void updatePwd(User user) throws Exception {
        Connection conn = dataSource.getConnection();
        try {
            String sql = "update tb_user set password = ? where account = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setObject(1, user.getPassword());
            pst.setObject(2, user.getAccount());
            pst.executeUpdate();
        } finally {
            conn.close();
        }
    }
}
