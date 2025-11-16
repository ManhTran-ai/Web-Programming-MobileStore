package com.mobilestore.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

/**
 * Cart entity - POJO class không sử dụng JPA annotations
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Cart {
    private Integer id;
    private Integer quantity;
    private Product product;
    private User user;
}
