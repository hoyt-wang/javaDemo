package com.kaishengit.weixin.mq;

import com.alibaba.fastjson.JSON;
import com.kaishengit.weixin.WeixinUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hoyt on 2017/11/22.
 */

@Component
public class WeixinConsumer {

    @Autowired
    private WeixinUtil weixinUtil;

    @JmsListener(destination = "weixinMessage-queue")
    public void  sendMessageToUser(String json) {
        Map<String,Object> map = JSON.parseObject(json, HashMap.class);
        weixinUtil.sendMessage(Arrays.asList(1,2,5),map.get("message").toString());
    }
}
