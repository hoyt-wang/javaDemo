package com.kaishengit.crm.controller;

import com.kaishengit.crm.auth.ShiroUtil;
import com.kaishengit.crm.controller.exception.ForbideenException;
import com.kaishengit.crm.controller.exception.NotFoundException;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

/**
 * 所有控制器的父类，提供共有的方法
 * Created by hoyt on 2017/11/10.
 */

public abstract class BaseController {
/*
    @Autowired
    private CustomerService customerService;*/
    /**
     * 获取当前登录系统的account对象
     * @param session
     * @return
     */
    public Account getCurrAccount(HttpSession session) {
        //return (Account) session.getAttribute("curr_account");
        return ShiroUtil.getCurrentAccount();
    }

  /*  *//**
     * 验证客户是否属于当前登录的对象
     * @param id
     * @param session
     * @return
     *//*
    public Customer validateCustomer(@PathVariable Integer id, HttpSession session) {
        //根据ID查找客户//根据ID查找客户
        Customer customer = customerService.findById(id);
        if(customer == null) {
            //404
            throw new NotFoundException("找不到" + id + "对应的客户");
        }
        Account account = getCurrAccount(session);
        if(!customer.getAccountId().equals(account.getId())) {
            //403
            throw new ForbideenException("没有查看客户"+ id + "的权限");
        }
        return customer;
    }*/
}
