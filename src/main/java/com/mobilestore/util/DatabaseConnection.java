package com.mobilestore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Thông tin kết nối database
    private static final String URL = "jdbc:mysql://localhost:3307/mobilestore"; // Thay đổi theo database của bạn
    private static final String USERNAME = "root"; // Username của bạn
    private static final String PASSWORD = "root"; // Password của bạn
    
    public static Connection getConnection() throws SQLException {
        try {
            // Load driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Tạo kết nối
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            if (connection != null) {
                System.out.println("✅ Kết nối database thành công!");
                return connection;
            } else {
                System.out.println("❌ Kết nối database thất bại!");
                return null;
            }
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Không tìm thấy driver: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("❌ Lỗi kết nối database: " + e.getMessage());
            throw e;
        }
    }
    
    // Method để test kết nối
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.out.println("❌ Test kết nối thất bại: " + e.getMessage());
            return false;
        }
    }
}