package com.example.highlevel.pojo;

import java.math.BigDecimal;

/**
 * @author Sebastian
 */
public class FoodDetail {

    private Integer foodId;
    
    private String foodName;
    
    private BigDecimal foodPrice;
    
    private String typeName;
    
    private String supplierName;

    public FoodDetail() {
    }

    public FoodDetail(Integer foodId, String foodName, BigDecimal foodPrice, String typeName, String supplierName) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.typeName = typeName;
        this.supplierName = supplierName;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public BigDecimal getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(BigDecimal foodPrice) {
        this.foodPrice = foodPrice;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
