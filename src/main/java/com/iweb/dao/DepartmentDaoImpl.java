package com.iweb.dao;

import com.iweb.dto.PageDto;
import com.iweb.model.Department;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoImpl implements DepartmentDao {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    public int selectCount() throws Exception {
        Connection conn = dataSource.getConnection();
        int count = 0;
        try {
            String sql = "select count(department_id) from tb_department";
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
    public List<Department> selectList(PageDto dto) throws Exception {
        List<Department> departments = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        try {
            String field = dto.getFiled();
            String order = dto.getOrder();
            String sql = "select * from tb_department order by " + field + " " + order + "  limit ?,?";
            int index = (dto.getIndex() - 1) * dto.getSize();
            int size = dto.getSize();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, index);
            pst.setInt(2, size);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Department department = new Department(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDouble(4));
                departments.add(department);
            }

        } finally {
            conn.close();
        }
        return departments;
    }
}
