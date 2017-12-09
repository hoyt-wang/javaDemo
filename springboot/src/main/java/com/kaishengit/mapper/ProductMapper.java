package com.kaishengit.mapper;

import com.kaishengit.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by hoyt on 2017/12/8.
 */
@Mapper
public interface ProductMapper {

    @Select("select * from t_product")
    List<Product> findAll();

    void save(String productName, Integer productInventory);

    void save(Product product);
}
