package com.kaishengit.crm.auth;

import com.kaishengit.crm.entity.Account;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;


/**
 * Created by hoyt on 2017/11/24.
 */

public class ShiroUtil {

    public static Account getCurrentAccount() {
        return (Account) getSubject().getPrincipal();
    }

    public static Subject getSubject() {
        return  SecurityUtils.getSubject();
    }
}
