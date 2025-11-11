package com.mobilestore.util;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("Đang kiểm tra kết nối database...");
        
        if (DatabaseConnection.testConnection()) {
            System.out.println("🎉 Database đã được kết nối thành công!");
        } else {
            System.out.println("💥 Không thể kết nối đến database!");
        }
    }
}