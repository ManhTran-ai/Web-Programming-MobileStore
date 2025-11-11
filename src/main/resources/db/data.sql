USE mobilestore;

-- Insert admin user (password: admin123)
INSERT INTO users (username, password, full_name, email, phone, address, role) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrator', 'admin@mobilestore.com', '0123456789', 'Hà Nội', 'ADMIN');

-- Insert categories
INSERT INTO categories (category_name, description) VALUES
('Điện thoại', 'Điện thoại di động các loại'),
('Tablet', 'Máy tính bảng'),
('Phụ kiện', 'Phụ kiện điện thoại');

-- Insert brands
INSERT INTO brands (brand_name, description) VALUES
('Apple', 'Thương hiệu công nghệ hàng đầu'),
('Samsung', 'Tập đoàn điện tử Hàn Quốc'),
('Xiaomi', 'Thương hiệu công nghệ Trung Quốc'),
('OPPO', 'Thương hiệu điện thoại'),
('Vivo', 'Thương hiệu điện thoại');