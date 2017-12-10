package com.kaishengit.crm.controller;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.service.AccountService;
import com.kaishengit.web.result.AjaxResult;
import com.kaishengit.web.result.DataTablesResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hoyt on 2017/11/8.
 */
@Controller
@RequestMapping("/employee")
public class AccountController {

    @Autowired
    private AccountService accountService;


    @GetMapping
    public String list() {
        return "employee/list";
    }

    @PostMapping("/dept/new")
    @ResponseBody
    public AjaxResult saveNewDept(String deptName) {
        try {
            accountService.saveNewDept(deptName);
            return AjaxResult.success();
        } catch (ServiceException e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 获取部门名称JSON数据
     * @return
     */
    @GetMapping("/dept.json")
    @ResponseBody
    public List<Dept> findAllDept() {
        return accountService.findAllDept();
    }

    /**
     * @param draw
     * @param start
     * @param length
     * @param deptId
     * @param request
     * @return DataTables
     */
    @GetMapping("/load.json")
    @ResponseBody
    public DataTablesResult<Account> loadEmployeeList(Integer draw, Integer start,
                                                      Integer length, Integer deptId,
                                                      HttpServletRequest request) {
        String keyWord = request.getParameter("search[value]");

        Map<String,Object> map = new HashMap<>();
        map.put("start",start);
        map.put("length",length);
        map.put("accountName",keyWord);
        map.put("deptId",deptId);
        List<Account> accountList = accountService.pageForAccount(map);
        Long total = accountService.accountCountByDeptId(deptId);
        return new DataTablesResult<>(draw,total.intValue(),accountList);
    }

    /**
     * @param id 根据id删除账户
     * @return
     */
    @PostMapping("/{id:\\d+}/delete")
    @ResponseBody
    public AjaxResult deleteEmployee(@PathVariable Integer id) {
        accountService.deleteEmployeeById(id);
        return AjaxResult.success();
    }

    /**
     * 添加新账号
     * @return
     */
    @PostMapping("/new")
    @ResponseBody
    public AjaxResult saveNewEmployee(String userName,String mobile,
                                      String password,Integer[] deptId) {
        try {
            accountService.saveNewEmployee(userName, mobile, password, deptId);
            return AjaxResult.success();
        } catch (ServiceException ex) {
            ex.printStackTrace();
            return AjaxResult.error(ex.getMessage());
        }

    }

}
