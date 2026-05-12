package com.iweb.service;

import com.iweb.dto.DataGrid;
import com.iweb.dto.PageDto;
import com.iweb.model.Department;

public interface DepartmentService {
    void findDepartmentList(DataGrid<Department> dataGrid, PageDto dto)throws Exception;
}
