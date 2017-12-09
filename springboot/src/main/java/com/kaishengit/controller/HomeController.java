package com.kaishengit.controller;

import com.kaishengit.dao.ProductDao;
import com.kaishengit.entity.Product;
import com.kaishengit.entity.User;
import com.kaishengit.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;


/**
 * Created by hoyt on 2017/12/7.
 */

@Controller
public class HomeController {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductDao productDao;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {

        model.addAttribute("user",new User(1001,"jack","南京"));
        model.addAttribute("msg","session message");
        model.addAttribute("message","spring boot version 01");
        model.addAttribute("productList",productMapper.findAll());
        List<String> nameList = Arrays.asList("a","b","c","d");
        model.addAttribute("nameList",nameList);
        return "index";
    }

    @GetMapping("/save")
    @ResponseBody
    public String save() {
        Product product = new Product();
        product.setProductName("东芝硬盘2T");
        product.setProductInventory(200);
        productMapper.save(product);
        //productDao.save("奥迪A8",180);
        return "saveProduct";
    }
}
