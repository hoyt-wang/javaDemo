package com.kaishengit.crm.auth;

import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.entity.Dept;
import com.kaishengit.crm.service.AccountService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoyt on 2017/11/24.
 */

public class ShrioRealm extends AuthorizingRealm {

    private AccountService accountService;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取当前登录的对象
        Account account = (Account) principalCollection.getPrimaryPrincipal();
        //根据登录的对象获取所在的部门列表
        List<Dept> deptList = accountService.findDeptByAccountId(account.getId());
        //获取Dept集合中的名称，创建字符串列表
        List<String> deptNameList = new ArrayList<>();
        for (Dept dept : deptList) {
            deptNameList.add(dept.getDeptName());
        }
        //将部门名称作为当前用户的角色
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addRoles(deptNameList);
        return simpleAuthorizationInfo;
    }


    /**
     * 登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String username = usernamePasswordToken.getUsername();
        Account account = accountService.findByMobile(username);
        if(account != null) {
            return new SimpleAuthenticationInfo(account,account.getPassword(),getName());
        }
        return null;
    }
}
