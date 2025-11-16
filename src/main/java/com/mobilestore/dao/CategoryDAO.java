package com.mobilestore.dao;

import com.mobilestore.entity.Category;
import com.mobilestore.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho Category entity sử dụng JDBC
 */
public class CategoryDAO {
    
    /**
     * Tìm category theo ID
     */
    public Category findById(Integer id) {
        String sql = "SELECT category_id, category_name FROM categories WHERE category_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm category theo ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Tìm category theo tên
     */
    public Category findByName(String name) {
        String sql = "SELECT category_id, category_name FROM categories WHERE category_name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, name);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm category theo tên: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả categories
     */
    public List<Category> findAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT category_id, category_name FROM categories ORDER BY category_name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả categories: " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }
    
    /**
     * Tạo category mới
     */
    public Category create(Category category) {
        String sql = "INSERT INTO categories (category_name) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, category.getName());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        category.setId(generatedKeys.getInt(1));
                        return category;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo category: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Cập nhật category
     */
    public boolean update(Category category) {
        String sql = "UPDATE categories SET category_name = ? WHERE category_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, category.getName());
            ps.setInt(2, category.getId());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật category: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa category
     */
    public boolean delete(Integer id) {
        String sql = "DELETE FROM categories WHERE category_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa category: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Map ResultSet thành Category object
     */
    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setId(rs.getInt("category_id"));
        category.setName(rs.getString("category_name"));
        return category;
    }
}

