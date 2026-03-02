package com.mobilestore.dao;

import com.mobilestore.entity.User;
import com.mobilestore.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho User entity sử dụng JDBC
 */
public class UserDAO {
    
    /**
     * Tìm user theo username
     * @param username Username cần tìm
     * @return User object nếu tìm thấy, null nếu không
     */
    public User findByUsername(String username) {
        String sql = "SELECT u.id, u.username, u.password, u.role_name, r.description as role_description, " +
                     "u.oauth_provider, u.oauth_id, u.email " +
                     "FROM users u " +
                     "LEFT JOIN roles r ON u.role_name = r.name " +
                     "WHERE u.username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm user theo username: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Tìm user theo ID
     * @param id ID của user
     * @return User object nếu tìm thấy, null nếu không
     */
    public User findById(Integer id) {
        String sql = "SELECT u.id, u.username, u.password, u.role_name, r.description as role_description, " +
                     "u.oauth_provider, u.oauth_id, u.email " +
                     "FROM users u " +
                     "LEFT JOIN roles r ON u.role_name = r.name " +
                     "WHERE u.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm user theo ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả users
     * @return List của tất cả users
     */
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.password, u.role_name, r.description as role_description, " +
                     "u.oauth_provider, u.oauth_id, u.email " +
                     "FROM users u " +
                     "LEFT JOIN roles r ON u.role_name = r.name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }
    
    /**
     * Tạo user mới
     * @param user User object cần tạo
     * @return User đã được tạo với ID, null nếu thất bại
     */
    public User create(User user) {
        String sql = "INSERT INTO users (username, password, role_name) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole() != null ? user.getRole().getName() : null);
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo user: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Cập nhật user
     * @param user User object cần cập nhật
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean update(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role_name = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole() != null ? user.getRole().getName() : null);
            ps.setInt(4, user.getId());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa user
     * @param id ID của user cần xóa
     * @return true nếu thành công, false nếu thất bại
     */
    public boolean delete(Integer id) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Map ResultSet thành User object
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        
        // Tạo Role object nếu có role_name
        String roleName = rs.getString("role_name");
        if (roleName != null) {
            com.mobilestore.entity.Role role = new com.mobilestore.entity.Role();
            role.setName(roleName);
            role.setDescription(rs.getString("role_description"));
            user.setRole(role);
        }
        
        // OAuth fields
        user.setOauthProvider(rs.getString("oauth_provider"));
        user.setOauthId(rs.getString("oauth_id"));
        user.setEmail(rs.getString("email"));
        
        return user;
    }
    
    /**
     * Tìm user theo oauth_id và oauth_provider
     * @param oauthId OAuth ID từ provider
     * @param oauthProvider Provider ('google', 'facebook')
     * @return User object nếu tìm thấy, null nếu không
     */
    public User findByOauthId(String oauthId, String oauthProvider) {
        String sql = "SELECT u.id, u.username, u.password, u.role_name, r.description as role_description, " +
                     "u.oauth_provider, u.oauth_id, u.email " +
                     "FROM users u " +
                     "LEFT JOIN roles r ON u.role_name = r.name " +
                     "WHERE u.oauth_id = ? AND u.oauth_provider = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, oauthId);
            ps.setString(2, oauthProvider);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm user theo oauth_id: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Tạo user mới từ OAuth (không có password)
     * @param user User object cần tạo
     * @return User đã được tạo với ID, null nếu thất bại
     */
    public User createWithOAuth(User user) {
        String sql = "INSERT INTO users (username, password, role_name, oauth_provider, oauth_id, email) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword() != null ? user.getPassword() : null); // Null password for OAuth
            ps.setString(3, user.getRole() != null ? user.getRole().getName() : "CUSTOMER");
            ps.setString(4, user.getOauthProvider());
            ps.setString(5, user.getOauthId());
            ps.setString(6, user.getEmail());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setId(generatedKeys.getInt(1));
                        return user;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo user OAuth: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

