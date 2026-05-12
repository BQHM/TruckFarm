package com.iweb.dao;

import com.iweb.dto.PageDto;
import com.iweb.model.Department;

import java.util.List;

public interface DepartmentDao {
    int selectCount() throws Exception;

    List<Department> selectList(PageDto dto) throws Exception;
}
