package com.mobilestore.filter;

import com.mobilestore.entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Filter kiểm tra quyền Admin cho các request tới /admin/*
 */
@WebFilter(filterName = "AdminAuthFilter", urlPatterns = {"/admin/*"})
public class AdminAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AdminAuthFilter đã được khởi tạo");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);

        // Kiểm tra session và user
        if (session == null || session.getAttribute("user") == null) {
            // Chưa đăng nhập, redirect về trang login
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login?error=unauthorized");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Kiểm tra role ADMIN
        if (user.getRole() == null || !"ADMIN".equals(user.getRole().getName())) {
            // Không có quyền Admin, redirect về trang chủ với thông báo lỗi
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/?error=access_denied");
            return;
        }

        // User là Admin, cho phép tiếp tục
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("AdminAuthFilter đã bị hủy");
    }
}

