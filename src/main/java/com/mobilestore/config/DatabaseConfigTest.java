package com.mobilestore.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConfigTest {
    public static void main(String[] args) {
        System.out.println("=== Kiểm tra kết nối Database ===\n");

        Connection conn = null;
        try {
            // Test connection
            conn = DatabaseConfig.getConnection();

            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Kết nối database THÀNH CÔNG!");
                System.out.println("Database: " + conn.getCatalog());
                System.out.println("URL: " + conn.getMetaData().getURL());
                System.out.println("Driver: " + conn.getMetaData().getDriverName());
                System.out.println("Version: " + conn.getMetaData().getDriverVersion());

                // Test query
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as total FROM users");

                if (rs.next()) {
                    System.out.println("\n✓ Test query THÀNH CÔNG!");
                    System.out.println("Số lượng users: " + rs.getInt("total"));
                }

                rs.close();
                stmt.close();
            }
        } catch (Exception e) {
            System.err.println("\n✗ Kết nối THẤT BẠI!");
            System.err.println("Lỗi: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DatabaseConfig.closeConnection(conn);
        }
    }
}
