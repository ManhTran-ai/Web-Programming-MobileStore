package com.mobilestore.service;

import com.mobilestore.entity.User;
import com.mobilestore.dao.UserDAO;
import com.mobilestore.util.PasswordUtil;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    public User authenticate(String username, String passwordPlain) {
        User user = userDAO.findByUsername(username);
        if (user == null)
            return null;

        // Sử dụng BCrypt để verify mật khẩu
        boolean matches = PasswordUtil.verifyPassword(passwordPlain, user.getPassword());
        return matches ? user : null;
    }
}
