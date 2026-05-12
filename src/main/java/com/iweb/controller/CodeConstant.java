package com.iweb.controller;

import com.iweb.dao.CodeDao;
import com.iweb.dao.CodeDaoImpl;
import com.iweb.model.Code;
import com.iweb.service.CodeService;
import com.iweb.service.CodeServiceImpl;

import java.util.*;

public class CodeConstant {
    private static CodeService codeService;
    private final static Set<String> usedCode = Collections.synchronizedSortedSet(new TreeSet<>());
    private final static Set<String> unUseCode = Collections.synchronizedSortedSet(new TreeSet<>());

    private final static Set<String> swapCode = new HashSet<>();

    public void setCodeService(CodeService codeService){
        this.codeService=codeService;
    }

    public void initCode() throws Exception{
        List<Code> codes = codeService.findAllCode();
        codes.forEach(CodeConstant::setCode);// 每个code取出来判断state的值
    }

    private static void setCode(Code code) {
        if (code.getState().equals("0")) {
            unUseCode.add(code.getCode());
        }
        if (code.getState().equals("1")) {
            usedCode.add(code.getCode());
        }
    }

    // 获取code
    public static String code() {
        Iterator<String> its = unUseCode.iterator();
        String code = "";
        if (!its.hasNext()) {
            throw new RuntimeException("记录已满");
        }
        code = its.next();
        unUseCode.remove(code);
        swapCode.add(code);
        return code;
    }

    //修改code使用情况
    public static void useCode(String code) throws Exception {
        codeService.modifyCodeState(code, "1");
        swapCode.remove(code);
        usedCode.add(code);
    }

    // 释放
    public static void freeCode(String code) throws Exception {
        codeService.modifyCodeState(code, "0");
        usedCode.remove(code);
        unUseCode.add(code);
    }

}

