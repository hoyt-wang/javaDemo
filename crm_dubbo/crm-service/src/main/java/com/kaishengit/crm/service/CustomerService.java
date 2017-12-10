package com.kaishengit.crm.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Customer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hoyt on 2017/11/9.
 */
public interface CustomerService {


    /**
     * @param id
     * @return customer
     */
    Customer findById(Integer id);

    /**
     * @return customer集合
     */
    PageInfo<Customer> findByAccountId(Integer pageNo);

    /**
     * 查找客户所属行业名称
     * @return
     */
    List<String> findAllCustomerTrade();

    /**
     * 查找客户来源名称
     * @return
     */
    List<String> findAllCustomerSource();
    /**
     * 新增客户
     * @param customer
     */
    void saveNewCustomer(Customer customer);

    /**
     * 删除客户
     * @param id
     */
    void delCustomerById(Integer id);

    /**
     * 编辑客户
     * @param customer
     */
    void editCustomer(Customer customer);

    /**
     * 根据当前账号查找对应的客户列表
     * @param pageNo
     * @param account
     * @return
     */
    PageInfo<Customer> pageForMyCustomer(Integer pageNo, Account account);

    /**
     * 将指定的客户放入公海
     * @param customer
     */
    void publicCustomer(Customer customer);

    /**
     * 转交客户
     * @param customer 被转交的客户
     * @param toAccountId 转入账号id
     */
    void transferCustomer(Customer customer, Integer toAccountId);

    /**
     * 导出客户资料文件为csv格式
     * @param outputStream
     * @param account
     */
    void exportCsvFileToOutputStream(OutputStream outputStream, Account account) throws IOException;

    /**
     * 导出客户资料文件为xls格式
     * @param outputStream
     * @param account
     */
    void exportXlsFileToOutputStream(OutputStream outputStream, Account account) throws IOException;

    /**
     * 根据当前登录账号id获得该账号客户列表
     * @param account
     * @return
     */
    List<Customer> findCustomerByAccountId(Account account);

    /**
     * 每月客户增加数量
     * @return
     */
    List<Map<String,Object>> countByCreateTime();
}
