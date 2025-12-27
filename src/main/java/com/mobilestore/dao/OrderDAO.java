package com.mobilestore.dao;

import com.mobilestore.entity.CartItem;
import com.mobilestore.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDAO {

    public Integer createOrder(int userId, double totalAmount, List<CartItem> items) {
        String orderSql = "INSERT INTO orders (order_status, order_date, total_amount, user_id) VALUES (?, ?, ?, ?)";
        String detailSql = "INSERT INTO order_details (price, quantity, order_id, product_id) VALUES (?, ?, ?, ?)";
        String updateProductSql = "UPDATE products SET quantity_in_stock = quantity_in_stock - ? WHERE product_id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psOrder = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setString(1, "PENDING");
                psOrder.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                psOrder.setDouble(3, totalAmount);
                psOrder.setInt(4, userId);
                psOrder.executeUpdate();

                try (ResultSet rs = psOrder.getGeneratedKeys()) {
                    if (rs.next()) {
                        int orderId = rs.getInt(1);

                        try (PreparedStatement psDetail = conn.prepareStatement(detailSql);
                             PreparedStatement psUpdateProduct = conn.prepareStatement(updateProductSql)) {
                            for (CartItem item : items) {
                                psDetail.setDouble(1, item.getProduct().getPrice());
                                psDetail.setInt(2, item.getQuantity());
                                psDetail.setInt(3, orderId);
                                psDetail.setInt(4, item.getProduct().getId());
                                psDetail.addBatch();

                                psUpdateProduct.setInt(1, item.getQuantity());
                                psUpdateProduct.setInt(2, item.getProduct().getId());
                                psUpdateProduct.addBatch();
                            }
                            psDetail.executeBatch();
                            psUpdateProduct.executeBatch();
                        }

                        conn.commit();
                        return orderId;
                    }
                }
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi OrderDAO.createOrder: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}


