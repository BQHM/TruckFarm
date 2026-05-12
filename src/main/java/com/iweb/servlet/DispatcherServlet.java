package com.iweb.servlet;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.iweb.controller.*;
import com.iweb.dao.*;
import com.iweb.dto.DataGrid;
import com.iweb.dto.PageDto;
import com.iweb.dto.Result;
import com.iweb.model.*;
import com.iweb.service.*;
import com.iweb.utils.DataSourceUtil;
import com.iweb.utils.JSONWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


public class DispatcherServlet extends HttpServlet {

    private LoginController loginController;

    private UserController userController;
    private PlantsController plantsController;
    private MenuController menuController;
    private DepartmentController departmentController;
    private CodeConstant codeConstant;

    @Override
    public void init(ServletConfig config) throws ServletException {
        DataSource dataSource = DataSourceUtil.initDataSource(config);
        //初始化
        // 登录界面
        loginController = new LoginController();
        UserServiceImpl userService = new UserServiceImpl();
        UserDaoImpl userDao = new UserDaoImpl();

        // 用户界面
        userController = new UserController();

        // 菜单界面
        menuController = new MenuController();
        MenuServiceImpl menuService = new MenuServiceImpl();
        MenuDaoImpl menuDao = new MenuDaoImpl();

        // 部门界面
        departmentController = new DepartmentController();
        DepartmentServiceImpl departmentService = new DepartmentServiceImpl();
        DepartmentDaoImpl departmentDao = new DepartmentDaoImpl();


        // 农作物界面
        plantsController = new PlantsController();
        PlantsServiceImpl plantsService = new PlantsServiceImpl();
        PlantsDaoImpl plantsDao = new PlantsDaoImpl();

        // 编号界面
        codeConstant = new CodeConstant();
        CodeServiceImpl codeService = new CodeServiceImpl();
        CodeDaoImpl codeDao = new CodeDaoImpl();




        // 处理依赖关系
        // 登录数据
        userDao.setDataSource(dataSource);
        userService.setUserDao(userDao);
        loginController.setUserService(userService);

        // 用户数据
        userController.setUserService(userService);

        // 农作物数据
        plantsDao.setDataSource(dataSource);
        plantsService.setPlantsDao(plantsDao);
        plantsController.setPlantsService(plantsService);

        // 菜单数据
        menuController.setMenuService(menuService);
        menuService.setMenuDao(menuDao);
        menuDao.setDataSource(dataSource);

        // 部门数据
        departmentController.setDepartmentService(departmentService);
        departmentService.setDepartmentDao(departmentDao);
        departmentDao.setDataSource(dataSource);

        // 编号数据
        codeConstant.setCodeService(codeService);
        codeService.setCodeDao(codeDao);
        codeDao.setDataSource(dataSource);


    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String url = req.getRequestURI();
        String[] urls = url.split("/");
        String ctl = null;
        String mtd = null;
        if (urls.length == 3) {
            ctl = urls[1];
            mtd = urls[2];
        }
        //处理静态页面
        if (handlerHtml(req, resp, url)) return;
        try {
            // 登录请求
            if (urls.length != 3 && "/login".equals(url)) handlerLogin(req, resp);

            // 获取员工列表
            if ("user".equals(ctl) && "list".equals(mtd)) handlerUserList(req, resp);

            // 修改员工信息
            if ("user".equals(ctl) && "modify".equals(mtd)) handlerUserModify(req, resp);

            // 修改员工权限
            if ("user".equals(ctl) && "role".equals(mtd)) handlerRole(req, resp);

            // 新增员工
            if ("user".equals(ctl) && "save".equals(mtd)) handlerSaveUser(req, resp);

            // 获取员工部门
            if ("user".equals(ctl) && "department".equals(mtd)) handlerUserDepartment(req, resp);

            // 获取农作物列表
            if ("plants".equals(ctl) && "list".equals(mtd)) handlerPlantsList(req, resp);

            // 获取作物类别
            if ("plants".equals(ctl) && "class".equals(mtd)) handlerPlantsClass(req, resp);

            // 修改作物价格
            if ("plants".equals(ctl) && "modify".equals(mtd)) handlerPlantsModify(req, resp);

            // 删除作物
            if ("plants".equals(ctl) && "remove".equals(mtd)) handlerRemovePlants(req, resp);

            // 获取登录信息
            if ("user".equals(ctl) && "modifyPwd".equals(mtd)) handlerModifyPwd(req, resp);

            // 获取菜单
            if ("menu".equals(ctl) && "list".equals(mtd)) handlerMenuList(req, resp);

            // 获取部门菜单
            if ("department".equals(ctl) && "list".equals(mtd)) handlerDepartmentList(req, resp);


        } catch (Exception e) {
            Result result = new Result(false, e.getLocalizedMessage());
            JSONWriter.writeJson(result, resp);
            e.printStackTrace();
        }

    }

    private void handlerDepartmentList(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        int index = Convert.toInt(req.getParameter("pageIndex")) + 1;
        int size = Convert.toInt(req.getParameter("pageSize"));
        String filed = req.getParameter("sortField");
        String order = req.getParameter("sortOrder");

        PageDto dto = new PageDto();
        dto.setIndex(index);
        dto.setSize(size);
        dto.setFiled(filed);
        dto.setOrder(order);

        DataGrid<Department> dataGrid = null;

        dataGrid = departmentController.list(dto);

        JSONWriter.writeJson(dataGrid, resp);

    }

    private void handlerMenuList(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        HttpSession session = req.getSession();
        List<Menu> menus = menuController.menuList(session);
        JSONWriter.writeJson(menus, resp);
    }

    private void handlerModifyPwd(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String account = (String) req.getSession().getAttribute("account");

        String newPassword = req.getParameter("newPassword");
        User user = new User(account, newPassword);

        Result result = userController.modityPwd(user);
        JSONWriter.writeJson(result, resp);
    }

    private void handlerRemovePlants(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String id = req.getParameter("plantsId");
        Result result = plantsController.removePlants(id);

        // 响应视图和数据
        JSONWriter.writeJson(result, resp);
    }

    private void handlerPlantsModify(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String plantsId = req.getParameter("plantsId");
        String plantsName = req.getParameter("plantsName");
        Integer growthCycle = Convert.toInt(req.getParameter("growthCycle"));
        Double plantsPrice = Convert.toDouble(req.getParameter("plantsPrice"));
        String plantsClass = req.getParameter("plantsClass");
        Plants plants = new Plants(plantsId, plantsName, growthCycle, plantsPrice, plantsClass);
        Result result = plantsController.modifyByPlantsId(plants);
        JSONWriter.writeJson(result, resp);

    }


    private void handlerPlantsClass(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<Plants> plants = plantsController.plantsClass();
        JSONWriter.writeJson(plants, resp);
    }

    private void handlerPlantsList(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        int index = Convert.toInt(req.getParameter("pageIndex")) + 1;
        int size = Convert.toInt(req.getParameter("pageSize"));
        String filed = req.getParameter("sortField");
        String order = req.getParameter("sortOrder");

        PageDto dto = new PageDto();
        dto.setIndex(index);
        dto.setSize(size);
        dto.setFiled(filed);
        dto.setOrder(order);

        String action = req.getParameter("action");
        DataGrid<Plants> dataGrid = null;
        if ("searchPlantsName".equals(action)) {
            String plantsName = req.getParameter("plantsName");
            dataGrid = plantsController.listPlantsByName(dto, plantsName);
        }
        if ("searchPlantsClass".equals(action)) {
            String plantsClass = req.getParameter("plantsClass");
            dataGrid = plantsController.listPlantsByClass(dto, plantsClass);
        }
        if (Objects.isNull(action)) {
            dataGrid = plantsController.list(dto);
        }
        assert dataGrid != null;
        JSONWriter.writeJson(dataGrid, resp);


    }

    private void handlerRole(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        List<User> users = userController.userRole();
        JSONWriter.writeJson(users, resp);
    }

    private void handlerUserDepartment(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        List<User> users = userController.userDepartment();
        JSONWriter.writeJson(users, resp);

    }

    private void handlerSaveUser(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String account = req.getParameter("account");
        String password = req.getParameter("password");
        String userName = req.getParameter("userName");
        String phone = req.getParameter("phone");
        String departmentName = req.getParameter("departmentName");
        String roleName = req.getParameter("roleName");

        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        user.setUserName(userName);
        user.setPhone(phone);
        user.setDepartmentName(departmentName);
        user.setRoleName(roleName);


        Result result = userController.saveUser(user);
        JSONWriter.writeJson(result, resp);


    }

    private void handlerUserModify(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String account = req.getParameter("account");
        String userName = req.getParameter("userName");
        String phone = req.getParameter("phone");
        String departmentName = req.getParameter("departmentName");
        String dimission = req.getParameter("dimission");
        String roleName = req.getParameter("roleName");
        User user = new User(account, userName, phone, departmentName, dimission, roleName);
        Result result = userController.modifyById(user);
        JSONWriter.writeJson(result, resp);

    }

    private void handlerUserList(HttpServletRequest req, HttpServletResponse resp) throws Exception {

        int index = Convert.toInt(req.getParameter("pageIndex")) + 1;
        int size = Convert.toInt(req.getParameter("pageSize"));
        String filed = req.getParameter("sortField");
        String order = req.getParameter("sortOrder");

        PageDto dto = new PageDto();
        dto.setIndex(index);
        dto.setSize(size);
        dto.setFiled(filed);
        dto.setOrder(order);
        DataGrid<User> dataGrid = null;

        dataGrid = userController.list(dto);

        JSONWriter.writeJson(dataGrid, resp);

    }

    private boolean handlerHtml(HttpServletRequest req, HttpServletResponse resp, String url) throws ServletException, IOException {
        if (url.startsWith("/view")) {
            // view 请求都是请求页面
            String viewName = "/WEB-INF" + url + ".html";
            req.getRequestDispatcher(viewName).forward(req, resp);
            return true;
        }
        return false;
    }

    private void handlerLogin(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String account = req.getParameter("account");
        String password = req.getParameter("password");
        User user = new User(account, password);
        Result result = loginController.login(user, req.getSession());
        JSONWriter.writeJson(result, resp, "yyyy-MM-dd");

    }
}
