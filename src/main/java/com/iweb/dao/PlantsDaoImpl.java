package com.iweb.dao;

import com.iweb.dto.PageDto;
import com.iweb.model.Plants;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PlantsDaoImpl implements PlantsDao {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int selectCount() throws Exception {
        Connection conn = dataSource.getConnection();
        int count = 0;
        try {
            String sql = "select count(plants_id) from tb_plants";
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
    public List<Plants> selectList(PageDto dto) throws Exception {
        List<Plants> plants = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        try {
            String field = dto.getFiled();
            String order = dto.getOrder();
            String sql = "select * from tb_plants order by " + field + " " + order + "  limit ?,?";
            int index = (dto.getIndex() - 1) * dto.getSize();
            int size = dto.getSize();

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, index);
            pst.setInt(2, size);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Plants plant = new Plants(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDouble(4), rs.getString(5));
                plants.add(plant);
            }
        } finally {
            conn.close();
        }
        return plants;
    }

    @Override
    public List<Plants> selectPlantsClass() throws Exception {
        List<Plants> plants = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        try {
            String sql = "select distinct class from tb_plants";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Plants plant = new Plants(null, null, null, null, rs.getString(1));
                plants.add(plant);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            conn.close();
        }
        return plants;
    }

    @Override
    public void updateByPlantsId(Plants plants) throws Exception {
        Connection conn = dataSource.getConnection();
        try {
            String sql = "update tb_plants set price = ? where plants_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setObject(1, plants.getPlantsPrice());
            pst.setObject(2, plants.getPlantsId());
            pst.executeUpdate();
        } finally {
            conn.close();
        }
    }

    @Override
    public void deleteByPlantsId(String id) throws Exception {
        Connection conn = dataSource.getConnection();
        try {
            String sql = "delete from tb_plants where plants_id = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setObject(1, id);
            pst.executeUpdate();
        } finally {
            conn.close();
        }
    }

    @Override
    public List<Plants> selectListByName(PageDto dto, String plantsName) throws Exception {
        List<Plants> plants = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        try {


            String field = dto.getFiled();
            String order = dto.getOrder();
            String sql = "select * from tb_plants where name =? order by " + field + " " + order + "  limit ?,?";

            int index = (dto.getIndex() - 1) * dto.getSize();
            int size = dto.getSize();

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setObject(1, plantsName);
            pst.setInt(2, index);
            pst.setInt(3, size);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Plants plant = new Plants(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDouble(4), rs.getString(5));
                plants.add(plant);
            }
        } finally {
            conn.close();
        }
        return plants;
    }

    @Override
    public List<Plants> selectListByClass(PageDto dto, String plantsClass) throws Exception {
        List<Plants> plants = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        try {


            String field = dto.getFiled();
            String order = dto.getOrder();
            String sql = "select * from tb_plants where class =? order by " + field + " " + order + "  limit ?,?";

            int index = (dto.getIndex() - 1) * dto.getSize();
            int size = dto.getSize();

            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setObject(1, plantsClass);
            pst.setInt(2, index);
            pst.setInt(3, size);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Plants plant = new Plants(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getDouble(4), rs.getString(5));
                plants.add(plant);
            }
        } finally {
            conn.close();
        }
        return plants;
    }
}
