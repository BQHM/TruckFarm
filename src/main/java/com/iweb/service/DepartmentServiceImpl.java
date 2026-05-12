package com.iweb.service;

import com.iweb.dao.DepartmentDaoImpl;
import com.iweb.dto.DataGrid;
import com.iweb.dto.PageDto;
import com.iweb.model.Department;

import java.util.List;

public class DepartmentServiceImpl implements DepartmentService{

    private DepartmentDaoImpl departmentDao;

    public void setDepartmentDao(DepartmentDaoImpl departmentDao) {
        this.departmentDao = departmentDao;
    }

    @Override
    public void findDepartmentList(DataGrid<Department> dataGrid, PageDto dto) throws Exception {
        int total  =departmentDao.selectCount();

        List<Department> departments=departmentDao.selectList(dto);
        dataGrid.setTotal(total);
        dataGrid.setData(departments);
    }
}
