package com.mobilestore.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductFormData {
    private Integer id;
    private String productName;
    private String manufacturer;
    private String productCondition;
    private Float price;
    private Integer quantityInStock;
    private String productInfo;
    private String newCategoryName;
    private Integer categoryId;
    private List<String> errors = new ArrayList<>();

    public boolean isValid() {
        return errors.isEmpty();
    }

    public void addError(String error) {
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getProductCondition() { return productCondition; }
    public void setProductCondition(String productCondition) { this.productCondition = productCondition; }
    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }
    public Integer getQuantityInStock() { return quantityInStock; }
    public void setQuantityInStock(Integer quantityInStock) { this.quantityInStock = quantityInStock; }
    public String getProductInfo() { return productInfo; }
    public void setProductInfo(String productInfo) { this.productInfo = productInfo; }
    public String getNewCategoryName() { return newCategoryName; }
    public void setNewCategoryName(String newCategoryName) { this.newCategoryName = newCategoryName; }
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
}
