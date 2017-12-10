package com.kaishengit.crm.controller.interceptor;

import com.kaishengit.crm.entity.Account;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by hoyt on 2017/11/8.
 */

public class LoginInterceptor extends HandlerInterceptorAdapter{

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        //获取用户请求的路径
        String url = request.getRequestURI();
        //如果是静态路径，则放行
        if(url.startsWith("/static/")) {
            return true;
        }
        //如果是登录页面，则放行
        if("".equals(url) | "/".equals(url)) {
            return true;
        }
        //判断用户是否登录
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("curr_account");
        if(account == null) {
            String id = request.getParameter("id");
            if(id == null) {
                response.sendRedirect("/?  =" + url);
            } else {
                response.sendRedirect("/?callback=" + url +"?id=" + id);
            }
            return false;
            //response.sendRedirect("/");
            /*response.sendRedirect("/?callback=" + url);
            return false;*/
        } else {
            return true;
        }
    }
}
