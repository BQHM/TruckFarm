package com.iweb.controller;

import com.iweb.dto.DataGrid;
import com.iweb.dto.PageDto;
import com.iweb.dto.Result;
import com.iweb.model.Plants;
import com.iweb.model.User;
import com.iweb.service.PlantsService;

import java.util.List;

public class PlantsController {

    private PlantsService plantsService;

    public void setPlantsService(PlantsService plantsService) {
        this.plantsService = plantsService;
    }

    public DataGrid<Plants> list(PageDto dto) throws Exception {

        DataGrid<Plants> dataGrid = new DataGrid<>();
        plantsService.findPlantsList(dataGrid, dto);
        return dataGrid;

    }

    public List<Plants> plantsClass() throws Exception{
        return plantsService.findPlantsClass();
    }

    public Result modifyByPlantsId(Plants plants) throws Exception{
        plantsService.modyfyByPlantsId(plants);
        return new Result(true,"modify Plants");
    }

    public Result removePlants(String id) throws Exception{
        plantsService.removePlants(id);
        return new Result(true,"remove plants");
    }

    public DataGrid<Plants> listPlantsByName(PageDto dto, String plantsName) throws Exception{
        DataGrid<Plants> dataGrid =new DataGrid<>();
        plantsService.findPlantsListByName(dataGrid,dto,plantsName);
        return dataGrid;
    }

    public DataGrid<Plants> listPlantsByClass(PageDto dto, String plantsClass) throws Exception{
        DataGrid<Plants> dataGrid =new DataGrid<>();
        plantsService.findPlantsListByClass(dataGrid,dto,plantsClass);
        return dataGrid;
    }
}
