package com.kaishengit.crm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.AccountDeptKey;
import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.example.AccountDeptExample;
import com.kaishengit.crm.example.AccountExample;
import com.kaishengit.crm.example.DeptExample;
import com.kaishengit.crm.exception.AuthenticationException;
import com.kaishengit.crm.exception.ServiceException;
import com.kaishengit.crm.mapper.AccountDeptMapper;
import com.kaishengit.crm.mapper.AccountMapper;
import com.kaishengit.crm.mapper.DeptMapper;
import com.kaishengit.crm.service.AccountService;
import com.kaishengit.weixin.WeixinUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hoyt on 2017/11/7.
 */

@Service
public class AccountServiceImpl implements AccountService{

    private Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    /**
     * 公司id
     */
    private static final Integer COMPANY_ID = 1;

    @Autowired
    private  AccountMapper accountMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private AccountDeptMapper accountDeptMapper;

    @Autowired
    private WeixinUtil weixinUtil;

    @Value("#{'${user.password.salt}'}")
    private String salt;

    /**
     * 用户登录
     * @param mobile
     * @param password
     * @return 登录成功返回Account对象，失败抛出AuthenticationException异常
     */
    @Override
    public Account login(String mobile, String password) throws AuthenticationException {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andMobileEqualTo(mobile);

        List<Account> accounts = accountMapper.selectByExample(accountExample);
        Account account = null;
        if(accounts != null && !accounts.isEmpty()) {
            account = accounts.get(0);
        }
        String encodePassword = DigestUtils.md5Hex(salt + password);
        if(account != null && encodePassword.equals(account.getPassword())) {
            logger.info("{}在{}登录成功",account.getUserName(),new Date());
            return account;
        } else {
            throw new AuthenticationException("用户名或者密码错误");
        }
    }

    /**
     * 查询所有部门
     * @return
     */
    @Override
    public List<Dept> findAllDept() {
        return deptMapper.selectByExample(new DeptExample());
    }

    /**
     * 添加新部门
     * @param deptName 部门名称
     * @throws ServiceException eg:名字已存在
     */
    @Override
    public void saveNewDept(String deptName) throws ServiceException {
        //判断deptName是否存在
        DeptExample deptExample = new DeptExample();
        deptExample.createCriteria().andDeptNameEqualTo(deptName);
        List<Dept> deptList = deptMapper.selectByExample(deptExample);
        Dept dept = null;
        if(deptList != null && !deptList.isEmpty()) {
            dept = deptList.get(0);
        }
        //添加新部门，使用公司ID作为父ID
        if(dept != null) {
            throw new ServiceException("该部门已存在");
        }
        dept = new Dept();
        dept.setDeptName(deptName);
        dept.setpId(COMPANY_ID);
        deptMapper.insertSelective(dept);
        //发送到微信
        weixinUtil.createDept(dept.getId(),dept.getpId(),dept.getDeptName());

        logger.info("添加新部门 {}",deptName);
    }

    /**
     * @param param 查询参数
     * @return account分页对象
     */
    @Override
    public List<Account> pageForAccount(Map<String, Object> param) {
        Integer start = (Integer) param.get("start");
        Integer length = (Integer) param.get("length");
        Integer deptId = (Integer) param.get("deptId");
        String accountName = (String) param.get("accountName");

        if(deptId == null || COMPANY_ID.equals(deptId)) {
            deptId = null;
        }

        List<Account> accountList = accountMapper.findByParam(accountName,deptId,start,length);

        return accountList;

        //PageHelper.offsetPage(start,length);

       /* AccountExample accountExample = new AccountExample();
        AccountExample.Criteria criteria = accountExample.createCriteria();

        if(StringUtils.isNotEmpty(accountName)) {
            criteria.andUserNameLike("%" + accountName + "%");
        }

        List<Account> accountList = accountMapper.selectByExample(accountExample);
        return new PageInfo<>(accountList);*/
    }

    /**
     * 根据部门ID获得账号数量
     * @param deptId
     * @return
     */
    @Override
    public Long accountCountByDeptId(Integer deptId) {

        if(deptId == null || COMPANY_ID.equals(deptId)) {
            deptId = null;
        }
        return accountMapper.countByDeptId(deptId);
    }

    /**
     * 根据id删除账号
     * @param id
     */
    @Override
    public void deleteEmployeeById(Integer id) {
        //1.TODO 判断其他的关联系
        //2.删除account_dept的关联关系
        AccountDeptExample accountDeptExample = new AccountDeptExample();
        accountDeptExample.createCriteria().andAccountIdEqualTo(id);
        //3.删除账号
        accountMapper.deleteByPrimaryKey(id);
    }

    /**
     * 添加新员工
     * @param userName
     * @param mobile
     * @param password
     */
    @Override
    @Transactional
    public void saveNewEmployee(String userName, String mobile, String password, Integer[] deptIds) {

        //1.验证手机号是否被使用
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andMobileEqualTo(mobile);

        List<Account> accountList = accountMapper.selectByExample(accountExample);
        if(accountList != null && !accountList.isEmpty()) {
            throw new ServiceException("该手机号已被使用");
        }
        //2.保存账号
        Account account = new Account();
        account.setUserName(userName);
        account.setPassword(password);
        account.setCreateTime(new Date());
        account.setUpdateTime(new Date());
        account.setMobile(mobile);

        accountMapper.insertSelective(account);
        //3.添加账号和部门的关系
        for(Integer deptId : deptIds) {
            AccountDeptKey accountDeptKey = new AccountDeptKey();
            accountDeptKey.setAccountId(account.getId());
            accountDeptKey.setDeptId(deptId);
            accountDeptMapper.insert(accountDeptKey);
        }
        //添加账号到微信
        weixinUtil.createAccount(account.getId(),userName,mobile, Arrays.asList(deptIds));
        logger.info("添加新账号 {}",userName);
    }

    /**
     * 获得所有账号
     * @return
     */
    @Override
    public List<Account> findAllAccount() {
        return accountMapper.selectByExample(new AccountExample());
    }

    /**
     * 更改密码
     * @param newPassword
     * @param confirmPassword
     */
    @Override
    public void changePassword(Account account, String password,String newPassword, String confirmPassword) {
        String oldpwd = account.getPassword();
        String MDoldpwd = DigestUtils.md5Hex(salt + password);
        if(MDoldpwd.equals(oldpwd)){ //输入的旧密码与原密码一致
            if(newPassword.equals(confirmPassword)){//判断输入的两个新密码是否一致
                if(!(DigestUtils.md5Hex(salt + newPassword).equals(oldpwd))){//如果新密码与原密码不同，执行更新密码操作
                    account.setPassword(DigestUtils.md5Hex(salt + newPassword));
                    accountMapper.updateByPrimaryKey(account);
                }else if(DigestUtils.md5Hex(salt + newPassword).equals(oldpwd)){
                    throw new ServiceException("密码没有改动");
                }
            }else{//抛出异常
                throw new ServiceException("抱歉，密码输入不一致");
            }
        }else{//抛出异常
            throw new ServiceException("旧密码输入错误");
        }
    }

    /**
     * 根据手机号查找账号
     * @param mobile
     * @return
     */
    @Override
    public Account findByMobile(String mobile) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andMobileEqualTo(mobile);
        List<Account> accountList = accountMapper.selectByExample(accountExample);
        if(accountList != null && !accountList.isEmpty()) {
            return accountList.get(0);
        }
        return null;
    }

    /**
     * 根据id获得所有所属部门
     *
     * @param accountId
     * @return
     */
    @Override
    public List<Dept> findDeptByAccountId(Integer accountId) {
        return deptMapper.findDeptByAccountId(accountId);
    }


}
