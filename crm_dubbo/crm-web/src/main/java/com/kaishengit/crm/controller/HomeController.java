package com.kaishengit.crm.controller;

import com.kaishengit.crm.auth.ShiroUtil;
import com.kaishengit.crm.entity.Account;
import com.kaishengit.crm.service.AccountService;
import com.kaishengit.web.result.AjaxResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * Created by hoyt on 2017/11/7.
 */

@Controller
public class HomeController {

    @Autowired
   private AccountService accountService;



    /**
     * 登录页
     * @return
     */
    @GetMapping("/")
    public String login() {
        Subject subject = ShiroUtil.getSubject();
        //System.out.println("is Authenticated ?" + subject.isAuthenticated());
        //System.out.println("is Remembered ?" + subject.isRemembered());
        if(subject.isAuthenticated()) {
            subject.logout();
        }
        if(!subject.isAuthenticated() && subject.isRemembered()) {
            return "redirect:/home";
        }
        return "index";
    }

    /**
     * 登录验证
     * @param mobile
     * @param password
     * @param redirectAttributes
     * @param request
     * @return
     */
    @PostMapping("/")
    public String login(String mobile, String password,
                        boolean rememberMe,
                        RedirectAttributes redirectAttributes,
                        HttpServletRequest request) {
        try {
           /* Account account =accountService.login(mobile,password);
            session.setAttribute("curr_account",account);*/
            //String saltPassword = salt + password;

            Subject subject = SecurityUtils.getSubject();
            String salt = "%$%$1234asdaDFG%%^@#SDF#$#%";
            //UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(mobile,new Md5Hash(password).toString());
            UsernamePasswordToken  usernamePasswordToken =
                    new UsernamePasswordToken(mobile,new SimpleHash("MD5",password,salt).toString(),rememberMe);

            subject.login(usernamePasswordToken);

            //跳转到登录前访问的URL
            String url = "/home";
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            if(savedRequest != null) {
                url = savedRequest.getRequestUrl();
            }
            return "redirect:" + url;

        } catch (AuthenticationException e) {
            redirectAttributes.addFlashAttribute("message","账号或者密码错误");
            return "redirect:/";
        }
    }

    /**
     * 登录后的页面
     * @return
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }

    /**
     * 安全退出
     * @param session
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        SecurityUtils.getSubject().logout();
        //session.invalidate();
        redirectAttributes.addFlashAttribute("message","您已退出该管理系统");
        return "redirect:/";

    }

    /**
     * 更改密码
     * @param session
     * @return
     */
    @GetMapping("/profile")
    public String changePassword(HttpSession session,Model model) {
        Account account = (Account) session.getAttribute("curr_account");
        model.addAttribute("account",account);
        return "profile";
    }

    @PostMapping("/profile")
    @ResponseBody
    public AjaxResult changePassword(HttpSession session, String newPassword, String confirmPassword,
                                     String password) {
        Account account = (Account) session.getAttribute("curr_account");
        try {
            accountService.changePassword(account,password,newPassword,confirmPassword);
            //重新登录
            session.invalidate();
            return AjaxResult.success();
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }

    }


}
