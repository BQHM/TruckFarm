package com.iweb.service;


import com.iweb.dao.CodeDao;
import com.iweb.model.Code;

import java.util.List;

public class CodeServiceImpl implements CodeService {

    private CodeDao codeDao;

    public void setCodeDao(CodeDao codeDao) {
        this.codeDao = codeDao;
    }


    @Override
    public void modifyCodeState(String code, String state) throws Exception{
        codeDao.updateCodeState(code,state);
    }

    @Override
    public List<Code> findAllCode() throws Exception {
        return codeDao.selectAllCode();
    }
}
