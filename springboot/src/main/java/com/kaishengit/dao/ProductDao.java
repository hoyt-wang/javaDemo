package com.kaishengit.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by hoyt on 2017/12/8.
 */

@Repository
public class ProductDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(String productName, Integer productInventory) {
        String sql = "insert into t_product(product_name,product_inventory) values(?,?)";
        jdbcTemplate.update(sql,productName,productInventory);
    }
}
