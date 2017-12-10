package com.kaishengit.weixin;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kaishengit.weixin.exception.WeixinException;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by hoyt on 2017/11/20.
 */

@Component
public class WeixinUtil {

    public static final String ACCESSTOKEN_TYPE_NORMAL = "normal";
    public static final String ACCESSTOKEN_TYPE_CONTACTS = "contacts";

    private static final String GET_ACCESS_TOKEN_URL="https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=%s&corpsecret=%s";

    private static final String POST_CREATE_DEPT_URL="https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token=%s";

    private static final String GET_DELETE_DEPT_URL="https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token=%s&id=%s";

    private static final String POST_CREATE_ACCOUNT_URL="https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token=%s";

    private static final String GET_DELETE_ACCOUNT_URL="https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token=%s&userid=%s";

    private static final String POST_SEND_MESSAGE_URL="https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=%s";

    @Value("${weixin.corpid}")
    private String corpid;

    @Value("${weixin.corpsecret}")
    private String corpsecret;

    @Value("${weixin.agentId}")
    private String agentId;

    @Value("${weixin.contacts.secret}")
    private String contactsSecret;

    /**
     * AccessToken的缓存
     */
    private LoadingCache<String,String> accessTokenCache = CacheBuilder.newBuilder()
            .expireAfterWrite(7200, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {
                @Override
                public String load(String type) throws Exception {
                    String url = "";
                    if(ACCESSTOKEN_TYPE_CONTACTS.equals(type)) {
                        url = String.format(GET_ACCESS_TOKEN_URL,corpid,contactsSecret);
                    } else {
                        url = String.format(GET_ACCESS_TOKEN_URL,corpid,corpsecret);
                    }
                    String resultJson = sendHttpGetRequest(url);
                    Map<String,Object> map = JSON.parseObject(resultJson, HashMap.class);
                    if(map.get("errcode").equals(0)) {
                        return (String) map.get("access_token");
                    }
                    throw new WeixinException(resultJson);
                }
            });

    /**
     * 获取AccessToken
     * @return
     */
    public String getAccessToken(String type) {
        try {
            return accessTokenCache.get(type);
        } catch (ExecutionException e) {
            throw new RuntimeException("获取AccessToken异常",e);
        }
    }


    /**
     * 创建部门
     * @param id
     * @param parentId
     * @param name
     */
    public void createDept(Integer id ,Integer parentId, String name) {
        String url = String.format(POST_CREATE_DEPT_URL,getAccessToken(ACCESSTOKEN_TYPE_CONTACTS));
        Map<String,Object> data = new HashMap<String, Object>();
        data.put("id",id);
        data.put("parentid",parentId);
        data.put("name",name);
        String resultIson = sendHttpPostRequest(url,JSON.toJSONString(data));
        Map<String,Object> resultMap = JSON.parseObject(resultIson,HashMap.class);
        if(!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("创建部门失败" + resultIson);
        }
    }

    /**
     * 删除部门
     * @param id
     */
    public void delDept(Integer id) {
        String url = String.format(GET_DELETE_DEPT_URL,getAccessToken(ACCESSTOKEN_TYPE_CONTACTS),id);
        String resultJson = sendHttpGetRequest(url);
        Map<String,Object> resultMap = JSON.parseObject(resultJson,HashMap.class);
        if(!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("删除部门异常: " + resultJson);
        }
    }

    /**
     * 创建员工账号
     * @param accountId
     * @param name
     * @param mobile
     * @param deptIdList
     */
    public void createAccount(Integer accountId, String name, String mobile, List<Integer> deptIdList) {
        String url = String.format(POST_CREATE_ACCOUNT_URL,getAccessToken(ACCESSTOKEN_TYPE_CONTACTS));
        Map<String,Object> data = new HashMap<String, Object>();
        data.put("userid",accountId);
        data.put("name",name);
        data.put("mobile",mobile);
        data.put("department",deptIdList);
        String resultJson = sendHttpPostRequest(url,JSON.toJSONString(data));
        Map<String,Object> resultMap = JSON.parseObject(resultJson,HashMap.class);

        if(!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("创建员工异常: " + resultJson);
        }
    }

    /**
     * 删除员工
     * @param id
     */
    public void delAccount(Integer id) {
        String url = String.format(GET_DELETE_ACCOUNT_URL,getAccessToken(ACCESSTOKEN_TYPE_CONTACTS),id);
        String resultJson = sendHttpGetRequest(url);
        Map<String,Object> resultMap = JSON.parseObject(resultJson,HashMap.class);
        if(!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("删除账号失败: " + resultJson);
        }
    }

    /**
     * 发送消息给用户
     * @param userIdList
     * @param message
     */
    public void sendMessage(List<Integer> userIdList,String message) {
        String url = String.format(POST_SEND_MESSAGE_URL,getAccessToken(ACCESSTOKEN_TYPE_NORMAL));
        StringBuilder stringBuilder = new StringBuilder();
        for (Integer userId : userIdList) {
            stringBuilder.append(userId).append("|");
        }
        String idString = stringBuilder.toString();
        idString = idString.substring(0,idString.lastIndexOf("|"));

        Map<String,Object> data = new HashMap<String, Object>();
        data.put("touser","WangHui");
        data.put("msgtype","text");
        data.put("agentid",agentId);
        Map<String,String> messageMap = new HashMap<String, String>();
        messageMap.put("content",message);
        data.put("text",messageMap);

        String resultJson = sendHttpPostRequest(url, JSON.toJSONString(data));

        Map<String, Object> resultMap = JSON.parseObject(resultJson, HashMap.class);
        if (!resultMap.get("errcode").equals(0)) {
            throw new WeixinException("发送文本消息失败: " + resultJson);
        }

    }


    /**
     * 发出Http的get请求
     * @param url 请求的url地址
     * @return
     */
    private String sendHttpGetRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException ex) {
            throw new RuntimeException("HTTP请求异常",ex);
        }
    }


    /**
     * 发出Http的Post请求
     */
    private String sendHttpPostRequest(String url, String json) {

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        //通过json格式构建post请求体
        RequestBody requestBody = RequestBody.create(JSON,json);
        Request request =  new Request.Builder().url(url).post(requestBody).build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            throw new WeixinException("HTTP请求异常",e);
        }

    }


}
