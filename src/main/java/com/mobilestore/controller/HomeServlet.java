package com.mobilestore.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet xử lý trang chủ của ứng dụng
 * 
 * @author Mobile Store Team
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/", "/home", "/index"})
public class HomeServlet extends HttpServlet {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Khởi tạo servlet
     */
    @Override
    public void init() throws ServletException {
        super.init();
        System.out.println("HomeServlet đã được khởi tạo");
    }
    
    /**
     * Xử lý GET request - hiển thị trang chủ
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Thiết lập encoding
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        
        // Lấy thông tin từ request (nếu có)
        String action = request.getParameter("action");
        
        // Xử lý các action khác nhau
        if (action != null) {
            switch (action) {
                case "about":
                    request.getRequestDispatcher("/views/common/about.jsp").forward(request, response);
                    return;
                case "contact":
                    request.getRequestDispatcher("/views/common/contact.jsp").forward(request, response);
                    return;
                default:
                    // Mặc định hiển thị trang chủ
                    break;
            }
        }
        
        // Forward đến trang chủ
        request.getRequestDispatcher("/views/common/home.jsp").forward(request, response);
    }
    
    /**
     * Xử lý POST request (nếu cần)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Chuyển về GET nếu cần
        doGet(request, response);
    }
    
    /**
     * Dọn dẹp khi servlet bị hủy
     */
    @Override
    public void destroy() {
        super.destroy();
        System.out.println("HomeServlet đã bị hủy");
    }
}

