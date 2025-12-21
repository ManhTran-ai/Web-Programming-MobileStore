package com.mobilestore.controller.admin;

import com.mobilestore.dao.CategoryDAO;
import com.mobilestore.dao.ProductDAO;
import com.mobilestore.entity.Category;
import com.mobilestore.entity.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * Servlet xử lý quản lý sản phẩm cho Admin
 * Sử dụng Jakarta Servlet 6.0 native Part API cho file upload
 */
@WebServlet(name = "AdminProductServlet", urlPatterns = {"/admin/products", "/admin/products/*"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB - lưu vào memory trước khi ghi ra disk
    maxFileSize = 1024 * 1024 * 10,        // 10 MB - max size cho 1 file
    maxRequestSize = 1024 * 1024 * 50      // 50 MB - max size cho toàn bộ request
)
public class AdminProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    // Thư mục lưu ảnh sản phẩm (đường dẫn tương đối trong webapp, được lưu vào DB)
    private static final String UPLOAD_DIR = "images/products";
    // UPLOAD_ROOT: đặt thành đường dẫn tuyệt đối tới thư mục webapp source để lưu trực tiếp vào source
    // Bạn yêu cầu lưu ảnh vào: D:\Web-Programming-MobileStore\src\main\webapp\images\products
    // Do đó UPLOAD_ROOT sẽ trỏ tới webapp root:
    private static final String UPLOAD_ROOT = "D:\\\\Web-Programming-MobileStore\\\\src\\\\main\\\\webapp";

    // Allowed image extensions
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(
        Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".webp")
    );

    // Allowed MIME types
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(
        Arrays.asList("image/jpeg", "image/png", "image/gif", "image/webp")
    );

    // Max file size (10MB)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            showProductList(request, response);
        } else if (pathInfo.equals("/add")) {
            showAddForm(request, response);
        } else if (pathInfo.equals("/edit")) {
            showEditForm(request, response);
        } else if (pathInfo.equals("/delete")) {
            deleteProduct(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.equals("/add")) {
            processAddProduct(request, response);
        } else if (pathInfo != null && pathInfo.equals("/edit")) {
            processEditProduct(request, response);
        } else if (pathInfo != null && pathInfo.equals("/delete")) {
            // Hỗ trợ POST cho delete (more secure)
            processDeleteProduct(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Hiển thị danh sách sản phẩm
     */
    private void showProductList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productDAO.findAll();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/views/admin/products/product-list.jsp").forward(request, response);
    }

    /**
     * Hiển thị form thêm sản phẩm
     */
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("categories", categories);
        request.setAttribute("isEdit", false);
        request.getRequestDispatcher("/views/admin/products/product-form.jsp").forward(request, response);
    }

    /**
     * Hiển thị form sửa sản phẩm
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");

        // Validate ID
        ValidationResult idValidation = validateId(idParam);
        if (!idValidation.isValid()) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=" + idValidation.getErrorCode());
            return;
        }

        Integer id = Integer.parseInt(idParam);
        Product product = productDAO.findById(id);

        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=not_found");
            return;
        }

        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("product", product);
        request.setAttribute("categories", categories);
        request.setAttribute("isEdit", true);
        request.getRequestDispatcher("/views/admin/products/product-form.jsp").forward(request, response);
    }

    /**
     * Xử lý thêm sản phẩm mới
     */
    private void processAddProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy và validate dữ liệu từ form
        ProductFormData formData = extractAndValidateFormData(request, false);

        if (!formData.isValid()) {
            setErrorAndForward(request, response, formData.getErrors(), formData);
            return;
        }

        try {
            // Xử lý upload hình ảnh sử dụng Jakarta Part API
            FileUploadResult uploadResult = processImageUpload(request);
            if (!uploadResult.isSuccess() && uploadResult.getErrorMessage() != null) {
                formData.addError(uploadResult.getErrorMessage());
                setErrorAndForward(request, response, formData.getErrors(), formData);
                return;
            }

            // Tạo đối tượng Product
            Product product = new Product();
            product.setProductName(formData.getProductName());
            product.setManufacturer(formData.getManufacturer());
            product.setProductCondition(formData.getProductCondition());
            product.setPrice(formData.getPrice());
            product.setQuantityInStock(formData.getQuantityInStock());
            product.setProductInfo(formData.getProductInfo());
            product.setImage(uploadResult.getFilePath());

            Category category = new Category();
            category.setId(formData.getCategoryId());
            product.setCategory(category);
            // Kiểm tra sản phẩm đã tồn tại (theo tên, nhà sản xuất, tình trạng, danh mục)
            Product existing = productDAO.findByUniqueKey(product.getProductName(),
                    product.getManufacturer(),
                    product.getProductCondition(),
                    product.getCategory() != null ? product.getCategory().getId() : null);

            if (existing != null) {
                // Nếu tồn tại, chỉ tăng số lượng
                int newQty = existing.getQuantityInStock() + product.getQuantityInStock();
                existing.setQuantityInStock(newQty);
                boolean updated = productDAO.update(existing);
                if (updated) {
                    response.sendRedirect(request.getContextPath() + "/admin/products?success=quantity_updated");
                } else {
                    formData.addError("Không thể cập nhật số lượng sản phẩm. Vui lòng thử lại.");
                    setErrorAndForward(request, response, formData.getErrors(), formData);
                }
                return;
            }

            // Nếu chưa tồn tại, lưu sản phẩm mới
            Product createdProduct = productDAO.create(product);

            if (createdProduct != null) {
                response.sendRedirect(request.getContextPath() + "/admin/products?success=created");
            } else {
                formData.addError("Không thể tạo sản phẩm. Vui lòng thử lại.");
                setErrorAndForward(request, response, formData.getErrors(), formData);
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi thêm sản phẩm: " + e.getMessage());
            e.printStackTrace();
            formData.addError("Lỗi hệ thống: " + e.getMessage());
            setErrorAndForward(request, response, formData.getErrors(), formData);
        }
    }

    /**
     * Xử lý cập nhật sản phẩm
     */
    private void processEditProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");
        ValidationResult idValidation = validateId(idStr);
        if (!idValidation.isValid()) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=" + idValidation.getErrorCode());
            return;
        }

        Integer id = Integer.parseInt(idStr);
        Product existingProduct = productDAO.findById(id);

        if (existingProduct == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=not_found");
            return;
        }

        // Lấy và validate dữ liệu từ form
        ProductFormData formData = extractAndValidateFormData(request, true);
        formData.setId(id);

        if (!formData.isValid()) {
            setErrorAndForwardEdit(request, response, formData.getErrors(), formData, id);
            return;
        }

        try {
            // Xử lý upload hình ảnh sử dụng Jakarta Part API
            FileUploadResult uploadResult = processImageUpload(request);
            if (!uploadResult.isSuccess() && uploadResult.getErrorMessage() != null) {
                formData.addError(uploadResult.getErrorMessage());
                setErrorAndForwardEdit(request, response, formData.getErrors(), formData, id);
                return;
            }

            // Nếu không upload ảnh mới, giữ ảnh cũ
            String imagePath = uploadResult.getFilePath();
            if (imagePath == null || imagePath.isEmpty()) {
                imagePath = existingProduct.getImage();
            } else {
                // Xóa ảnh cũ nếu có ảnh mới
                deleteOldImage(existingProduct.getImage());
            }

            // Cập nhật đối tượng Product
            existingProduct.setProductName(formData.getProductName());
            existingProduct.setManufacturer(formData.getManufacturer());
            existingProduct.setProductCondition(formData.getProductCondition());
            existingProduct.setPrice(formData.getPrice());
            existingProduct.setQuantityInStock(formData.getQuantityInStock());
            existingProduct.setProductInfo(formData.getProductInfo());
            existingProduct.setImage(imagePath);

            Category category = new Category();
            category.setId(formData.getCategoryId());
            existingProduct.setCategory(category);

            // Cập nhật database
            boolean updated = productDAO.update(existingProduct);

            if (updated) {
                response.sendRedirect(request.getContextPath() + "/admin/products?success=updated");
            } else {
                formData.addError("Không thể cập nhật sản phẩm. Vui lòng thử lại.");
                setErrorAndForwardEdit(request, response, formData.getErrors(), formData, id);
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/products?error=system");
        }
    }

    /**
     * Xử lý xóa sản phẩm (GET request)
     */
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idParam = request.getParameter("id");
        String confirm = request.getParameter("confirm");

        ValidationResult idValidation = validateId(idParam);
        if (!idValidation.isValid()) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=" + idValidation.getErrorCode());
            return;
        }

        // Yêu cầu xác nhận qua parameter
        if (!"true".equals(confirm)) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=confirm_required&id=" + idParam);
            return;
        }

        Integer id = Integer.parseInt(idParam);
        performDelete(id, request, response);
    }

    /**
     * Xử lý xóa sản phẩm (POST request - more secure)
     */
    private void processDeleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String idParam = request.getParameter("id");

        ValidationResult idValidation = validateId(idParam);
        if (!idValidation.isValid()) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=" + idValidation.getErrorCode());
            return;
        }

        Integer id = Integer.parseInt(idParam);
        performDelete(id, request, response);
    }

    /**
     * Thực hiện xóa sản phẩm
     */
    private void performDelete(Integer id, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        // Lấy thông tin sản phẩm trước khi xóa
        Product product = productDAO.findById(id);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=not_found");
            return;
        }

        // Xóa sản phẩm
        boolean deleted = productDAO.delete(id);

        if (deleted) {
            // Xóa file ảnh nếu có
            deleteOldImage(product.getImage());
            response.sendRedirect(request.getContextPath() + "/admin/products?success=deleted");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/products?error=delete_failed");
        }
    }

    /**
     * Xử lý upload hình ảnh sử dụng Jakarta Servlet 6.0 Part API
     */
    private FileUploadResult processImageUpload(HttpServletRequest request) throws IOException, ServletException {
        Part filePart = request.getPart("image");

        // Kiểm tra file có được upload không
        if (filePart == null || filePart.getSize() == 0) {
            return new FileUploadResult(true, null, null); // No file uploaded, không phải lỗi
        }

        // Sử dụng Part.getSubmittedFileName() - Jakarta Servlet 6.0 native method
        String fileName = filePart.getSubmittedFileName();
        if (fileName == null || fileName.trim().isEmpty()) {
            return new FileUploadResult(true, null, null);
        }

        // Validate file size
        if (filePart.getSize() > MAX_FILE_SIZE) {
            return new FileUploadResult(false, null, "Kích thước file không được vượt quá 10MB");
        }

        // Validate file extension
        String extension = getFileExtension(fileName).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            return new FileUploadResult(false, null,
                "Định dạng file không được hỗ trợ. Chỉ chấp nhận: JPG, JPEG, PNG, GIF, WEBP");
        }

        // Validate MIME type
        String contentType = filePart.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            return new FileUploadResult(false, null,
                "Loại file không hợp lệ. Chỉ chấp nhận file ảnh.");
        }

        // Tạo tên file unique để tránh trùng lặp và bảo mật
        String uniqueFileName = UUID.randomUUID().toString() + extension;

        // Lấy đường dẫn thực tế trên server hoặc sử dụng UPLOAD_ROOT nếu được cấu hình (giữ nguyên UPLOAD_DIR làm đường dẫn tương đối để lưu vào DB)
        String uploadPath;
        if (UPLOAD_ROOT != null && !UPLOAD_ROOT.trim().isEmpty()) {
            uploadPath = UPLOAD_ROOT + File.separator + UPLOAD_DIR;
        } else {
            uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
        }

        // Tạo thư mục nếu chưa tồn tại
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            boolean created = uploadDir.mkdirs();
            if (!created) {
                return new FileUploadResult(false, null, "Không thể tạo thư mục upload");
            }
        }

        // Lưu file sử dụng Part.write() - Jakarta Servlet native method
        Path filePath = Paths.get(uploadPath, uniqueFileName);
        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        // Trả về đường dẫn tương đối (lưu vào DB) - đảm bảo không có leading slash
        String dbPath = UPLOAD_DIR + "/" + uniqueFileName;
        if (dbPath.startsWith("/")) {
            dbPath = dbPath.substring(1);
        }

        // Log thông tin lưu file để debug
        System.out.println("Uploaded file saved to filesystem: " + filePath.toAbsolutePath());
        System.out.println("Image DB path to store: " + dbPath);

        return new FileUploadResult(true, dbPath, null);
    }

    /**
     * Xóa file ảnh cũ
     */
    private void deleteOldImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            return;
        }

        try {
            // Try configured upload root first, fallback to servlet context real path
            String fullPath = null;
            if (UPLOAD_ROOT != null && !UPLOAD_ROOT.trim().isEmpty()) {
                fullPath = UPLOAD_ROOT + File.separator + imagePath;
            }
            File file = null;
            if (fullPath != null) {
                file = new File(fullPath);
            }
            if (file == null || !file.exists()) {
                String fallback = getServletContext().getRealPath("") + File.separator + imagePath;
                file = new File(fallback);
            }
            if (file == null) {
                return;
            }
            String actualPath = file.getAbsolutePath();
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    System.err.println("Không thể xóa file: " + actualPath);
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa file ảnh: " + e.getMessage());
        }
    }

    /**
     * Lấy extension của file
     */
    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf(".");
        if (lastDot > 0) {
            return fileName.substring(lastDot);
        }
        return "";
    }

    /**
     * Extract và validate dữ liệu từ form
     */
    private ProductFormData extractAndValidateFormData(HttpServletRequest request, boolean isEdit) {
        ProductFormData formData = new ProductFormData();

        // Extract data
        String productName = request.getParameter("productName");
        String manufacturer = request.getParameter("manufacturer");
        String productCondition = request.getParameter("productCondition");
        String priceStr = request.getParameter("price");
        String quantityStr = request.getParameter("quantityInStock");
        String productInfo = request.getParameter("productInfo");
        String categoryIdStr = request.getParameter("categoryId");
        String newCategoryName = request.getParameter("newCategoryName");

        // Set raw values for re-display
        formData.setProductName(productName != null ? productName.trim() : "");
        formData.setManufacturer(manufacturer != null ? manufacturer.trim() : "");
        formData.setProductCondition(productCondition != null ? productCondition.trim() : "Mới");
        formData.setProductInfo(productInfo != null ? productInfo.trim() : "");
        formData.setNewCategoryName(newCategoryName != null ? newCategoryName.trim() : "");

        // Validate productName
        if (productName == null || productName.trim().isEmpty()) {
            formData.addError("Tên sản phẩm không được để trống");
        } else if (productName.trim().length() < 2) {
            formData.addError("Tên sản phẩm phải có ít nhất 2 ký tự");
        } else if (productName.trim().length() > 255) {
            formData.addError("Tên sản phẩm không được vượt quá 255 ký tự");
        }

        // Validate manufacturer
        if (manufacturer == null || manufacturer.trim().isEmpty()) {
            formData.addError("Nhà sản xuất không được để trống");
        } else if (manufacturer.trim().length() > 255) {
            formData.addError("Tên nhà sản xuất không được vượt quá 255 ký tự");
        }

        // Validate price
        if (priceStr == null || priceStr.trim().isEmpty()) {
            formData.addError("Giá không được để trống");
        } else {
            try {
                float price = Float.parseFloat(priceStr.trim());
                if (price < 0) {
                    formData.addError("Giá phải lớn hơn hoặc bằng 0");
                } else if (price > 999999999) {
                    formData.addError("Giá không được vượt quá 999,999,999 VNĐ");
                } else {
                    formData.setPrice(price);
                }
            } catch (NumberFormatException e) {
                formData.addError("Giá không hợp lệ. Vui lòng nhập số");
            }
        }

        // Validate quantity
        if (quantityStr == null || quantityStr.trim().isEmpty()) {
            formData.addError("Số lượng không được để trống");
        } else {
            try {
                int quantity = Integer.parseInt(quantityStr.trim());
                if (quantity < 0) {
                    formData.addError("Số lượng phải lớn hơn hoặc bằng 0");
                } else if (quantity > 99999) {
                    formData.addError("Số lượng không được vượt quá 99,999");
                } else {
                    formData.setQuantityInStock(quantity);
                }
            } catch (NumberFormatException e) {
                formData.addError("Số lượng không hợp lệ. Vui lòng nhập số nguyên");
            }
        }

        // Validate category selection OR new category creation
        if ((categoryIdStr == null || categoryIdStr.trim().isEmpty())
                && (newCategoryName == null || newCategoryName.trim().isEmpty())) {
            formData.addError("Vui lòng chọn danh mục hoặc nhập tên danh mục mới");
        } else if (newCategoryName != null && !newCategoryName.trim().isEmpty()) {
            // Create or reuse category by name
            String name = newCategoryName.trim();
            if (name.length() > 255) {
                formData.addError("Tên danh mục không được vượt quá 255 ký tự");
            } else {
                Category existing = categoryDAO.findByName(name);
                if (existing != null) {
                    formData.setCategoryId(existing.getId());
                } else {
                    Category toCreate = new Category();
                    toCreate.setName(name);
                    Category created = categoryDAO.create(toCreate);
                    if (created == null) {
                        formData.addError("Không thể tạo danh mục mới. Vui lòng thử lại.");
                    } else {
                        formData.setCategoryId(created.getId());
                    }
                }
            }
        } else {
            // Use selected category id
            try {
                int categoryId = Integer.parseInt(categoryIdStr.trim());
                if (categoryId <= 0) {
                    formData.addError("Danh mục không hợp lệ");
                } else {
                    // Kiểm tra category có tồn tại không
                    Category category = categoryDAO.findById(categoryId);
                    if (category == null) {
                        formData.addError("Danh mục không tồn tại");
                    } else {
                        formData.setCategoryId(categoryId);
                    }
                }
            } catch (NumberFormatException e) {
                formData.addError("Danh mục không hợp lệ");
            }
        }

        // Validate productCondition
        if (productCondition == null || productCondition.trim().isEmpty()) {
            formData.setProductCondition("Mới");
        } else {
            List<String> validConditions = Arrays.asList("Mới", "Đã qua sử dụng", "Tân trang");
            if (!validConditions.contains(productCondition.trim())) {
                formData.addError("Tình trạng sản phẩm không hợp lệ");
            }
        }

        // Validate productInfo length
        if (productInfo != null && productInfo.length() > 1000) {
            formData.addError("Mô tả sản phẩm không được vượt quá 1000 ký tự");
        }

        return formData;
    }

    /**
     * Validate ID parameter
     */
    private ValidationResult validateId(String idParam) {
        if (idParam == null || idParam.trim().isEmpty()) {
            return new ValidationResult(false, "missing_id");
        }
        try {
            int id = Integer.parseInt(idParam.trim());
            if (id <= 0) {
                return new ValidationResult(false, "invalid_id");
            }
            return new ValidationResult(true, null);
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "invalid_id");
        }
    }

    /**
     * Set error và forward về form thêm sản phẩm
     */
    private void setErrorAndForward(HttpServletRequest request, HttpServletResponse response,
                                    List<String> errors, ProductFormData formData)
            throws ServletException, IOException {
        request.setAttribute("errors", errors);
        request.setAttribute("error", String.join("<br>", errors));
        request.setAttribute("formData", formData);
        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("categories", categories);
        request.setAttribute("isEdit", false);
        request.getRequestDispatcher("/views/admin/products/product-form.jsp").forward(request, response);
    }

    /**
     * Set error và forward về form sửa sản phẩm
     */
    private void setErrorAndForwardEdit(HttpServletRequest request, HttpServletResponse response,
                                        List<String> errors, ProductFormData formData, Integer id)
            throws ServletException, IOException {
        request.setAttribute("errors", errors);
        request.setAttribute("error", String.join("<br>", errors));

        // Tạo product object từ formData để hiển thị lại
        Product product = new Product();
        product.setId(id);
        product.setProductName(formData.getProductName());
        product.setManufacturer(formData.getManufacturer());
        product.setProductCondition(formData.getProductCondition());
        product.setPrice(formData.getPrice());
        product.setQuantityInStock(formData.getQuantityInStock());
        product.setProductInfo(formData.getProductInfo());

        if (formData.getCategoryId() != null) {
            Category category = new Category();
            category.setId(formData.getCategoryId());
            product.setCategory(category);
        }

        // Lấy ảnh cũ từ database
        Product existingProduct = productDAO.findById(id);
        if (existingProduct != null) {
            product.setImage(existingProduct.getImage());
        }

        request.setAttribute("product", product);
        List<Category> categories = categoryDAO.findAll();
        request.setAttribute("categories", categories);
        request.setAttribute("isEdit", true);
        request.getRequestDispatcher("/views/admin/products/product-form.jsp").forward(request, response);
    }

    // ==================== Inner Classes ====================

    /**
     * Class chứa kết quả upload file
     */
    private static class FileUploadResult {
        private final boolean success;
        private final String filePath;
        private final String errorMessage;

        public FileUploadResult(boolean success, String filePath, String errorMessage) {
            this.success = success;
            this.filePath = filePath;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() { return success; }
        public String getFilePath() { return filePath; }
        public String getErrorMessage() { return errorMessage; }
    }

    /**
     * Class chứa kết quả validation
     */
    private static class ValidationResult {
        private final boolean valid;
        private final String errorCode;

        public ValidationResult(boolean valid, String errorCode) {
            this.valid = valid;
            this.errorCode = errorCode;
        }

        public boolean isValid() { return valid; }
        public String getErrorCode() { return errorCode; }
    }

    /**
     * Class chứa dữ liệu form và validation errors
     */
    public static class ProductFormData {
        private Integer id;
        private String productName;
        private String manufacturer;
        private String productCondition;
        private Float price;
        private Integer quantityInStock;
        private String productInfo;
        private String newCategoryName;
        private Integer categoryId;
        private List<String> errors = new ArrayList<>();

        public boolean isValid() { return errors.isEmpty(); }
        public void addError(String error) { errors.add(error); }
        public List<String> getErrors() { return errors; }

        // Getters and Setters
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public String getManufacturer() { return manufacturer; }
        public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

        public String getProductCondition() { return productCondition; }
        public void setProductCondition(String productCondition) { this.productCondition = productCondition; }

        public Float getPrice() { return price; }
        public void setPrice(Float price) { this.price = price; }

        public Integer getQuantityInStock() { return quantityInStock; }
        public void setQuantityInStock(Integer quantityInStock) { this.quantityInStock = quantityInStock; }

        public String getProductInfo() { return productInfo; }
        public void setProductInfo(String productInfo) { this.productInfo = productInfo; }

        public Integer getCategoryId() { return categoryId; }
        public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
        public String getNewCategoryName() { return newCategoryName; }
        public void setNewCategoryName(String newCategoryName) { this.newCategoryName = newCategoryName; }
    }
}

