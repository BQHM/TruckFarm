package com.iweb.dao;

import com.iweb.dto.PageDto;
import com.iweb.model.Plants;

import java.util.List;

public interface PlantsDao {
    int selectCount() throws Exception;

    List<Plants> selectList(PageDto dto) throws Exception;

    List<Plants> selectPlantsClass() throws Exception;

    void updateByPlantsId(Plants plants) throws Exception;

    void deleteByPlantsId(String id) throws Exception;

    List<Plants> selectListByName(PageDto dto, String plantsName) throws Exception;

    List<Plants> selectListByClass(PageDto dto, String plantsClass) throws Exception;
}
