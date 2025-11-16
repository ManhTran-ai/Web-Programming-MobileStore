package com.mobilestore.repository;

import com.mobilestore.dao.UserDAO;
import com.mobilestore.entity.User;

/**
 * Repository class cho User - sử dụng JDBC thông qua UserDAO
 */
public class UserRepository {
    private final UserDAO userDAO = new UserDAO();
    
    /**
     * Tìm user theo username
     * @param username Username cần tìm
     * @return User object nếu tìm thấy, null nếu không
     */
    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }
    
    /**
     * Tìm user theo ID
     * @param id ID của user
     * @return User object nếu tìm thấy, null nếu không
     */
    public User findById(Integer id) {
        return userDAO.findById(id);
    }
}
