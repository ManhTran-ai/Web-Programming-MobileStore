package com.mobilestore.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class để mã hóa và xác thực mật khẩu sử dụng BCrypt
 */
public class PasswordUtil {
    
    /**
     * Hash mật khẩu bằng BCrypt
     * @param plainPassword Mật khẩu dạng plain text
     * @return Mật khẩu đã được hash
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        // BCrypt tự động tạo salt và hash mật khẩu
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    
    /**
     * Kiểm tra mật khẩu có khớp với hash đã lưu không
     * @param plainPassword Mật khẩu dạng plain text cần kiểm tra
     * @param hashedPassword Mật khẩu đã được hash trong database
     * @return true nếu mật khẩu khớp, false nếu không
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            System.err.println("Lỗi khi verify mật khẩu: " + e.getMessage());
            return false;
        }
    }
}

