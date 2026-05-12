package com.iweb.service;

import com.iweb.dao.PlantsDao;
import com.iweb.dto.DataGrid;
import com.iweb.dto.PageDto;
import com.iweb.model.Plants;

import java.util.List;


public class PlantsServiceImpl implements PlantsService{

    private PlantsDao plantsDao;

    public void setPlantsDao(PlantsDao plantsDao){
        this.plantsDao=plantsDao;
    }

    @Override
    public void findPlantsList(DataGrid<Plants> dataGrid, PageDto dto) throws Exception {
        // 查询总数
        int total = plantsDao.selectCount();
        // 分页查询
        List<Plants> plants = plantsDao.selectList(dto);
        dataGrid.setTotal(total);
        dataGrid.setData(plants);

    }

    @Override
    public List<Plants> findPlantsClass() throws Exception {
        return plantsDao.selectPlantsClass();
    }

    @Override
    public void modyfyByPlantsId(Plants plants) throws Exception {
        plantsDao.updateByPlantsId(plants);
    }

    @Override
    public void removePlants(String id) throws Exception {
        plantsDao.deleteByPlantsId(id);
    }

    @Override
    public void findPlantsListByName(DataGrid<Plants> dataGrid, PageDto dto,String plantsName) throws Exception {
        int total = plantsDao.selectCount();
        // 分页查询
        List<Plants> plants = plantsDao.selectListByName(dto,plantsName);
        dataGrid.setTotal(total);
        dataGrid.setData(plants);
    }

    @Override
    public void findPlantsListByClass(DataGrid<Plants> dataGrid, PageDto dto, String plantsClass) throws Exception {
        int total = plantsDao.selectCount();
        List<Plants> plants = plantsDao.selectListByClass(dto,plantsClass);
        dataGrid.setTotal(total);
        dataGrid.setData(plants);
    }
}
