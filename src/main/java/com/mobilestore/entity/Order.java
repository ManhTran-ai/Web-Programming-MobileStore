package com.mobilestore.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * Order entity - POJO class không sử dụng JPA annotations
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Order {
    private Integer id;
    private String orderStatus;
    private Date orderDate;
    private Double totalAmount;
    private User user;
    private List<OrderDetail> details;
}
