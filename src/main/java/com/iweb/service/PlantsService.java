package com.iweb.service;

import com.iweb.dto.DataGrid;
import com.iweb.dto.PageDto;
import com.iweb.model.Plants;

import java.util.List;

public interface PlantsService {
    void findPlantsList(DataGrid<Plants> dataGrid, PageDto dto) throws Exception;

    List<Plants> findPlantsClass() throws Exception;

    void modyfyByPlantsId(Plants plants) throws Exception;

    void removePlants(String id) throws Exception;

    void findPlantsListByName(DataGrid<Plants> dataGrid, PageDto dto, String plantsName) throws Exception;

    void findPlantsListByClass(DataGrid<Plants> dataGrid, PageDto dto, String plantsClass) throws Exception;
}
