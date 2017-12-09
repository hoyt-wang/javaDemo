package com.kaishengit.entity;

/**
 * Created by hoyt on 2017/12/8.
 */

public class Product {

    private Integer id;
    private String productName;
    private Integer productInventory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductInventory() {
        return productInventory;
    }

    public void setProductInventory(Integer productInventory) {
        this.productInventory = productInventory;
    }
}
