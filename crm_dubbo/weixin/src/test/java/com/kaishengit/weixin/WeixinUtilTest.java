package com.kaishengit.weixin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * Created by hoyt on 2017/11/20.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-weixin.xml")
public class WeixinUtilTest {

    @Autowired
    private WeixinUtil weixinUtil;

    @Test
    public void getAccessToken() {
        String accessToken = weixinUtil.getAccessToken(WeixinUtil.ACCESSTOKEN_TYPE_CONTACTS);
        System.out.println(accessToken);
    }

    @Test
    public void createDept() {
        weixinUtil.createDept(002,1,"工程部");
    }

    @Test
    public void delDept() {
        weixinUtil.delDept(002);
    }

    @Test
    public void createAccount() {
        weixinUtil.createAccount(001,"jack","1212212", Arrays.asList(002));
    }

    @Test
    public void delAccount() {
        weixinUtil.delAccount(001);
    }

    @Test
    public void senMessage() {
        weixinUtil.sendMessage(Arrays.asList(001,002),"你有一个待办事项要处理，请点此<a href=\"http://www.baidu.com\" >链接</a>查看");
    }
}
