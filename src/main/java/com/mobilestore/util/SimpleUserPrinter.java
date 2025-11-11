package com.mobilestore.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SimpleUserPrinter {
    public static void main(String[] args) {
        String sql = "SELECT id, username, password, role_name FROM users";
        
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            System.out.println("\n📋 DANH SÁCH USERS:");
            System.out.println("=".repeat(60));
            
            int count = 0;
            while (resultSet.next()) {
                count++;
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String roleName = resultSet.getString("role_name");
                
                System.out.printf("User %d:%n", count);
                System.out.printf("  - ID: %d%n", id);
                System.out.printf("  - Username: %s%n", username);
                System.out.printf("  - Password: %s%n", password);
                System.out.printf("  - Role: %s%n", roleName);
                System.out.println("-".repeat(30));
            }
            
            if (count == 0) {
                System.out.println("❌ Không có dữ liệu trong bảng users!");
            } else {
                System.out.printf("✅ Tổng cộng: %d users%n", count);
            }
            
        } catch (SQLException e) {
            System.out.println("❌ Lỗi: " + e.getMessage());
        }
    }
}