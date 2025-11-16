package com.mobilestore.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * OrderDetail entity - POJO class không sử dụng JPA annotations
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDetail {
    private Integer id;
    private Double price;
    private Integer quantity;
    private Order order;
    private Product product;
}
