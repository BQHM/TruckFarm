package com.iweb.service;


import com.iweb.dao.CodeDao;
import com.iweb.model.Code;

import java.util.List;

public interface CodeService {


    public void modifyCodeState(String code, String state)throws Exception;

    List<Code> findAllCode()throws Exception;
}
