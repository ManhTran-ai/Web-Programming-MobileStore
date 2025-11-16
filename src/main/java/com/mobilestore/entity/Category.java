package com.mobilestore.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Category entity - POJO class không sử dụng JPA annotations
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private Integer id;
    private String name;
}
