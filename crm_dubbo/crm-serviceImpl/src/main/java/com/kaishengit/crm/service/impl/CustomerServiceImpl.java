package com.kaishengit.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Customer;
import com.kaishengit.crm.example.CustomerExample;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.mapper.AccountMapper;
import com.kaishengit.crm.mapper.CustomerMapper;
import com.kaishengit.crm.service.CustomerService;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hoyt on 2017/11/9.
 */

@Service
public class CustomerServiceImpl implements CustomerService {

    Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private AccountMapper accountMapper;

    //SpringEL

    @Value("#{'${customer.trade}'.split(',')}")
    private List<String> customerTrade;

    @Value("#{'${customer.source}'.split(',')}")
    private List<String> customerSource;


    /**
     * @param id
     * @return customer
     */
    @Override
    public Customer findById(Integer id) {
        return customerMapper.selectByPrimaryKey(id);
    }

    /**
     * @return customer集合
     */
    @Override
    public PageInfo<Customer> findByAccountId(Integer pageNo) {
        PageHelper.startPage(pageNo,5);
        List<Customer> list = customerMapper.selectByExample(new CustomerExample());
        return new PageInfo<>(list);
    }

    /**
     * 查找客户所属行业名称
     *
     * @return
     */
    @Override
    public List<String> findAllCustomerTrade() {
        return customerTrade;
    }

    /**
     * 查找客户来源名称
     *
     * @return
     */
    @Override
    public List<String> findAllCustomerSource() {
        return customerSource;
    }

    /**
     * 新增客户
     * @param customer
     */
    @Override
    public void saveNewCustomer(Customer customer) {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andMobileEqualTo(customer.getMobile());
        List<Customer> list  = customerMapper.selectByExample(customerExample);

        if(list != null && !list.isEmpty()) {
            throw  new ServiceException("该客户已存在");
        }
        customerMapper.insertSelective(customer);
    }

    /**
     * 删除客户
     * @param id
     */
    @Override
    public void delCustomerById(Integer id) {
        customerMapper.deleteByPrimaryKey(id);
    }

    /**
     * 编辑客户
     * @param customer
     */
    @Override
    public void editCustomer(Customer customer) {
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 根据当前账号查找对应的客户列表
     * @param pageNo
     * @param account
     * @return
     */
    @Override
    public PageInfo<Customer> pageForMyCustomer(Integer pageNo, Account account) {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andAccountIdEqualTo(account.getId());

        PageHelper.startPage(pageNo,15);
        List<Customer> list = customerMapper.selectByExample(customerExample);

        return new PageInfo<>(list);
    }

    /**
     * 将指定的客户放入公海
     * @param customer
     */
    @Override
    public void publicCustomer(Customer customer) {
        Account account = accountMapper.selectByPrimaryKey(customer.getAccountId());
        customer.setAccountId(null);
        customer.setReminder(customer.getReminder() + " " + account.getUserName() + "将该客户放入公海" );
        customerMapper.updateByPrimaryKey(customer);
    }

    /**
     * 转交客户
     * @param customer 被转交的客户
     * @param toAccountId 转入账号id
     */
    @Override
    public void transferCustomer(Customer customer, Integer toAccountId) {
        Account account = accountMapper.selectByPrimaryKey(customer.getAccountId());
        customer.setAccountId(toAccountId);
        customer.setReminder(customer.getReminder() + " " + "从" + account.getUserName() + "转交过来");
        customerMapper.updateByPrimaryKeySelective(customer);
    }

    /**
     * 导出客户资料文件为csv格式
     * @param outputStream
     * @param account
     */
    @Override
    public void exportCsvFileToOutputStream(OutputStream outputStream, Account account) throws IOException {
        List<Customer> customerList = findCustomerByAccountId(account);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("姓名")
                .append(",")
                .append("联系电话")
                .append(",")
                .append("职位")
                .append(",")
                .append("地址")
                .append("\r\n");
        for(Customer customer : customerList) {
            stringBuilder.append(customer.getCustName())
                    .append(",")
                    .append(customer.getMobile())
                    .append(",")
                    .append(customer.getJobTitle())
                    .append(",")
                    .append(customer.getAddress())
                    .append("\r\n");
        }
        IOUtils.write(stringBuilder.toString(),outputStream,"GBK");

        outputStream.flush();
        outputStream.close();
    }

    /**
     * 导出客户资料文件为xls格式
     * @param outputStream
     * @param account
     */
    @Override
    public void exportXlsFileToOutputStream(OutputStream outputStream, Account account) throws IOException {
        List<Customer> customerList = findCustomerByAccountId(account);

        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("我的客户");
        Row titleRow = sheet.createRow(0);
        Cell nameCell = titleRow.createCell(0);

        nameCell.setCellValue("姓名");
        titleRow.createCell(1).setCellValue("电话");
        titleRow.createCell(2).setCellValue("职位");
        titleRow.createCell(3).setCellValue("地址");

        for(int i = 0;i < customerList.size();i++) {
            Customer customer = customerList.get(i);
            Row dataRow = sheet.createRow(i+1);
            dataRow.createCell(0).setCellValue(customer.getCustName());
            dataRow.createCell(1).setCellValue(customer.getMobile());
            dataRow.createCell(2).setCellValue(customer.getJobTitle());
            dataRow.createCell(3).setCellValue(customer.getAddress());
        }
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 根据当前登录账号id获得该账号客户列表
     * @param account
     * @return
     */
    @Override
    public List<Customer> findCustomerByAccountId(Account account) {
        CustomerExample customerExample = new CustomerExample();
        customerExample.createCriteria().andAccountIdEqualTo(account.getId());
        List<Customer> customerList = customerMapper.selectByExample(customerExample);
        return  customerList;
    }

    /**
     * 每月客户增加数量
     *
     * @return
     */
    @Override
    public List<Map<String,Object>> countByCreateTime() {
        return customerMapper.countByCreateTime();
    }


}
