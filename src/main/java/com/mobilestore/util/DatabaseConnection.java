package com.mobilestore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class để quản lý kết nối database sử dụng JDBC
 */
public class DatabaseConnection {
    // Thông tin kết nối database
    private static final String URL = "jdbc:mysql://localhost:3306/mobilestore?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1317192005";
    
    // Load driver một lần khi class được load
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Không tìm thấy MySQL JDBC Driver", e);
        }
    }
    
    /**
     * Lấy connection từ database
     * @return Connection object
     * @throws SQLException nếu có lỗi khi kết nối
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            if (connection != null) {
                System.out.println("✅ Kết nối database thành công!");
            }
            return connection;
        } catch (SQLException e) {
            System.err.println("❌ Lỗi kết nối database: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Test kết nối database
     * @return true nếu kết nối thành công, false nếu không
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("❌ Test kết nối thất bại: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Đóng connection an toàn
     * @param connection Connection cần đóng
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("❌ Lỗi khi đóng connection: " + e.getMessage());
            }
        }
    }
}