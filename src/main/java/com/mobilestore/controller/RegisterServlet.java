package com.mobilestore.controller;

import com.mobilestore.entity.User;
import com.mobilestore.dao.UserDAO;
import com.mobilestore.util.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = "/register")
public class RegisterServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        if (username == null || username.trim().isEmpty()) {
            req.setAttribute("error", "Tên đăng nhập không được để trống");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            req.setAttribute("error", "Mật khẩu không được để trống");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        }

        if (password.length() < 6) {
            req.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        }

        if (!password.equals(confirmPassword)) {
            req.setAttribute("error", "Mật khẩu xác nhận không khớp");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        }

        if (userDAO.findByUsername(username) != null) {
            req.setAttribute("error", "Tên đăng nhập đã tồn tại");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
            return;
        }

        User newUser = new User();
        newUser.setUsername(username.trim());
        
        String hashedPassword = PasswordUtil.hashPassword(password);
        newUser.setPassword(hashedPassword);

        newUser.setRoleName("CUSTOMER");

        User createdUser = userDAO.create(newUser);
        
        if (createdUser != null) {
            req.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            req.getRequestDispatcher("/views/auth/login.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "Đã xảy ra lỗi khi đăng ký. Vui lòng thử lại.");
            req.getRequestDispatcher("/views/auth/register.jsp").forward(req, resp);
        }
    }
}
