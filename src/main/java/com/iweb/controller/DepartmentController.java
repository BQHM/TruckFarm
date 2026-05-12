package com.iweb.controller;

import com.iweb.dto.DataGrid;
import com.iweb.dto.PageDto;
import com.iweb.model.Department;
import com.iweb.service.DepartmentService;

public class DepartmentController {

    private DepartmentService departmentService;

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public DataGrid<Department> list(PageDto dto) throws Exception{
        DataGrid<Department> dataGrid =new DataGrid<>();
        departmentService.findDepartmentList(dataGrid,dto);
        return dataGrid;
    }
}
