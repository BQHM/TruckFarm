package com.iweb.dao;

import com.iweb.model.Code;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class CodeDaoImpl implements CodeDao {

    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void updateCodeState(String code, String state) throws Exception {
        Connection conn = dataSource.getConnection();
        try {

            String sql = "update tb_code set state = ? where code =?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, state);
            pst.setString(2, code);
            pst.executeUpdate();

        } finally {
            conn.close();
        }
    }

    @Override
    public List<Code> selectAllCode() throws Exception {

        List<Code> codes = new ArrayList<>();
        Connection conn = dataSource.getConnection();
        try {

            String sql="select * from tb_code";
            PreparedStatement

        } finally {
            conn.close();
        }

        return codes;
    }
}
