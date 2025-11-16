package com.mobilestore.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * Product entity - POJO class không sử dụng JPA annotations
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Product {
    private Integer id;
    private String productName;
    private String manufacturer;
    private String productCondition;
    private Float price;
    private String image;
    private String productInfo;
    private Integer quantityInStock;
    private Category category;
}
