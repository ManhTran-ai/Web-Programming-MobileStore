package com.mobilestore.controller;

import com.mobilestore.dao.ProductDAO;
import com.mobilestore.dao.CategoryDAO;
import com.mobilestore.entity.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ProductsServlet", urlPatterns = {"/products", "/products/*"})
public class ProductsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();

        // Kiểm tra xem có phải request đến trang chi tiết sản phẩm không
        if (requestURI.equals(contextPath + "/products/view")) {
            handleProductDetail(request, response);
            return;
        }

        // Xử lý danh sách sản phẩm (mặc định)
        int page = 1;
        int pageSize = 12;
        String pageParam = request.getParameter("page");
        String sizeParam = request.getParameter("size");
        String searchKeyword = request.getParameter("search");
        String categoryParam = request.getParameter("category");

        if (pageParam != null) {
            try { page = Math.max(1, Integer.parseInt(pageParam)); } catch (NumberFormatException ignored) {}
        }
        if (sizeParam != null) {
            try { pageSize = Math.max(1, Integer.parseInt(sizeParam)); } catch (NumberFormatException ignored) {}
        }

        ProductDAO productDAO = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        // Xử lý category filter
        Integer categoryId = null;
        if (categoryParam != null && !categoryParam.trim().isEmpty()) {
            try {
                categoryId = Integer.parseInt(categoryParam);
            } catch (NumberFormatException ignored) {}
        }

        List<Product> products;
        int totalItems;
        int totalPages;

        // Kiểm tra xem có tìm kiếm hay không
        if (searchKeyword != null && !searchKeyword.trim().isEmpty()) {
            // Tìm kiếm với từ khóa và category filter
            totalItems = productDAO.countSearch(searchKeyword.trim(), categoryId);
            totalPages = (int) Math.ceil((double) totalItems / pageSize);
            if (page > totalPages && totalPages > 0) page = totalPages;
            int offset = (page - 1) * pageSize;

            products = productDAO.searchWithFilter(searchKeyword.trim(), categoryId, offset, pageSize);

            request.setAttribute("searchKeyword", searchKeyword.trim());
        } else if (categoryId != null) {
            // Lọc theo category
            totalItems = productDAO.countSearch(null, categoryId);
            totalPages = (int) Math.ceil((double) totalItems / pageSize);
            if (page > totalPages && totalPages > 0) page = totalPages;
            int offset = (page - 1) * pageSize;

            products = productDAO.searchWithFilter(null, categoryId, offset, pageSize);
        } else {
            // Hiển thị tất cả sản phẩm
            totalItems = productDAO.countAll();
            totalPages = (int) Math.ceil((double) totalItems / pageSize);
            if (page > totalPages && totalPages > 0) page = totalPages;
            int offset = (page - 1) * pageSize;

            products = productDAO.findPage(offset, pageSize);
        }

        // Load danh sách categories cho dropdown filter
        List<com.mobilestore.entity.Category> categories = categoryDAO.findAll();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("totalItems", totalItems);
        request.setAttribute("selectedCategory", categoryId);

        request.getRequestDispatcher("/views/products/product-list.jsp").forward(request, response);
    }

    /**
     * Xử lý hiển thị chi tiết sản phẩm
     */
    private void handleProductDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idParam = request.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu tham số id sản phẩm");
            return;
        }

        try {
            Integer productId = Integer.parseInt(idParam.trim());
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.findById(productId);

            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Không tìm thấy sản phẩm");
                return;
            }


            List<Product> relatedProducts = new ArrayList<>();
            if (product.getCategory() != null && product.getCategory().getId() != null) {
                relatedProducts = productDAO.findByCategory(product.getCategory().getId())
                    .stream()
                    .filter(p -> !p.getId().equals(product.getId()))
                    .limit(10)
                    .toList();
            }

            request.setAttribute("product", product);
            request.setAttribute("relatedProducts", relatedProducts);
            request.getRequestDispatcher("/views/products/product-detail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm không hợp lệ");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}


