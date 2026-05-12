package com.iweb.dao;

import com.iweb.model.Code;

import java.util.List;

public interface CodeDao {

    void updateCodeState(String code, String state)throws Exception;

    List<Code> selectAllCode()throws Exception;
}
