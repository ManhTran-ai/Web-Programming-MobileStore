package com.mobilestore.dao;

import com.mobilestore.entity.Product;
import com.mobilestore.entity.Category;
import com.mobilestore.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object cho Product entity sử dụng JDBC
 */
public class ProductDAO {
    
    /**
     * Tìm product theo ID
     */
    public Product findById(Integer id) {
        String sql = "SELECT p.product_id, p.product_name, " +
                     "p.manufacturer, p.product_condition, " +
                     "p.price, p.image, p.product_info, p.quantity_in_stock, p.category_id, " +
                     "c.category_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.product_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm product theo ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Lấy tất cả products
     */
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_name, " +
                     "p.manufacturer, p.product_condition, " +
                     "p.price, p.image, p.product_info, p.quantity_in_stock, p.category_id, " +
                     "c.category_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "ORDER BY p.product_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy tất cả products: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }
    
    /**
     * Tìm products theo category
     */
    public List<Product> findByCategory(Integer categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_name, " +
                     "p.manufacturer, p.product_condition, " +
                     "p.price, p.image, p.product_info, p.quantity_in_stock, p.category_id, " +
                     "c.category_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.category_id = ? " +
                     "ORDER BY p.product_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, categoryId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm products theo category: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }
    
    /**
     * Tìm products theo tên (search)
     */
    public List<Product> searchByName(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_name, " +
                     "p.manufacturer, p.product_condition, " +
                     "p.price, p.image, p.product_info, p.quantity_in_stock, p.category_id, " +
                     "c.category_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "WHERE p.product_name LIKE ? OR p.product_info LIKE ? " +
                     "ORDER BY p.product_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm kiếm products: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }
    
    /**
     * Đếm tổng số sản phẩm
     */
    public int countAll() {
        String sql = "SELECT COUNT(*) AS total FROM products";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi đếm products: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy trang sản phẩm (offset, limit) sắp xếp giảm dần theo product_id
     */
    public List<Product> findPage(int offset, int limit) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.product_id, p.product_name, " +
                     "p.manufacturer, p.product_condition, " +
                     "p.price, p.image, p.product_info, p.quantity_in_stock, p.category_id, " +
                     "c.category_name " +
                     "FROM products p " +
                     "LEFT JOIN categories c ON p.category_id = c.category_id " +
                     "ORDER BY p.product_id DESC " +
                     "LIMIT ? OFFSET ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy trang products: " + e.getMessage());
            e.printStackTrace();
        }
        return products;
    }
    
    /**
     * Tạo product mới
     */
    public Product create(Product product) {
        String sql = "INSERT INTO products (product_name, manufacturer, product_condition, price, " +
                     "image, product_info, quantity_in_stock, category_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getManufacturer());
            ps.setString(3, product.getProductCondition());
            ps.setFloat(4, product.getPrice());
            // Ensure stored image path does not contain leading context path or leading slash
            String imageToStore = product.getImage();
            if (imageToStore != null && imageToStore.startsWith("/")) {
                imageToStore = imageToStore.substring(1);
            }
            ps.setString(5, imageToStore);
            ps.setString(6, product.getProductInfo());
            ps.setInt(7, product.getQuantityInStock());
            // Fix: Sử dụng setObject thay vì setInt để hỗ trợ null value
            if (product.getCategory() != null && product.getCategory().getId() != null) {
                ps.setInt(8, product.getCategory().getId());
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }

            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                        return product;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tạo product: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Cập nhật product
     */
    public boolean update(Product product) {
        String sql = "UPDATE products SET product_name = ?, manufacturer = ?, product_condition = ?, " +
                     "price = ?, image = ?, product_info = ?, quantity_in_stock = ?, category_id = ? " +
                     "WHERE product_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getManufacturer());
            ps.setString(3, product.getProductCondition());
            ps.setFloat(4, product.getPrice());
            ps.setString(5, product.getImage());
            ps.setString(6, product.getProductInfo());
            ps.setInt(7, product.getQuantityInStock());
            // Fix: Sử dụng setNull thay vì setInt để hỗ trợ null value
            if (product.getCategory() != null && product.getCategory().getId() != null) {
                ps.setInt(8, product.getCategory().getId());
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }
            ps.setInt(9, product.getId());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi cập nhật product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Xóa product
     */
    public boolean delete(Integer id) {
        String sql = "DELETE FROM products WHERE product_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa product: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Tìm product theo bộ khóa duy nhất (product_name, manufacturer, product_condition, category_id)
     * Trả về product nếu tồn tại, null nếu không tồn tại
     */
    public Product findByUniqueKey(String productName, String manufacturer, String productCondition, Integer categoryId) {
        StringBuilder sql = new StringBuilder("SELECT p.product_id, p.product_name, p.manufacturer, p.product_condition, ")
                .append("p.price, p.image, p.product_info, p.quantity_in_stock, p.category_id, c.category_name ")
                .append("FROM products p LEFT JOIN categories c ON p.category_id = c.category_id WHERE p.product_name = ? AND p.manufacturer = ? AND p.product_condition = ? ");

        if (categoryId == null) {
            sql.append("AND p.category_id IS NULL");
        } else {
            sql.append("AND p.category_id = ?");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, productName);
            ps.setString(2, manufacturer);
            ps.setString(3, productCondition);
            if (categoryId != null) {
                ps.setInt(4, categoryId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi khi tìm product theo unique key: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Map ResultSet thành Product object
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setProductName(rs.getString("product_name"));
        product.setManufacturer(rs.getString("manufacturer"));
        product.setProductCondition(rs.getString("product_condition"));
        product.setPrice(rs.getFloat("price"));
        String img = rs.getString("image");
        if (img != null) {
            // Normalize: remove leading slash if present so JSP path building stays consistent
            if (img.startsWith("/")) {
                img = img.substring(1);
            }
        }
        product.setImage(img);
        product.setProductInfo(rs.getString("product_info"));
        product.setQuantityInStock(rs.getInt("quantity_in_stock"));
        
        // Tạo Category object nếu có category_id
        Integer categoryId = rs.getInt("category_id");
        if (categoryId != null && !rs.wasNull()) {
            Category category = new Category();
            category.setId(categoryId);
            category.setName(rs.getString("category_name"));
            product.setCategory(category);
        }
        
        return product;
    }
}

