package com.kaishengit.service;

import com.kaishengit.entity.Product;
import com.kaishengit.service.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

/**
 * Created by hoyt on 2017/12/5.
 */

public interface ProductService {

    /**
     * 添加商品
     * @param product
     * @param inputStream
     */
    void saveProduct(Product product, InputStream inputStream);

    /**
     * 查找所有商品列表
     * @return
     */
    List<Product> findAll();

    /**
     * 根据id查找商品
     * @return
     * @param id
     */
    Product findById(Integer id);

    /**
     * 秒杀商品
     * @param id
     */
    void seckill(Integer id) throws ServiceException;
}
