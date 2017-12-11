package com.kaishengit.test;

import com.kaishengit.service.Config;
import com.kaishengit.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by hoyt on 2017/12/11.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
public class DubboTest {

    @com.alibaba.dubbo.config.annotation.Reference
    private ProductService productService;

    @Test
    public void findAllNames() {
        List<String> names = productService.findAllNames();
        for (String name : names) {
            System.out.println(name);
        }
    }
}
