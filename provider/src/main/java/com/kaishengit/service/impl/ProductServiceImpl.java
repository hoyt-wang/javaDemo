package com.kaishengit.service.impl;

import com.kaishengit.service.ProductService;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hoyt on 2017/12/8.
 */

public class ProductServiceImpl implements ProductService {
    public List<String> findAllNames() {
        return Arrays.asList("jack","alex","lebron","iron");
    }
}
