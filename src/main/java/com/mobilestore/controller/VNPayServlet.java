package com.mobilestore.controller;

import com.mobilestore.config.VNPayConfig;
import com.mobilestore.dao.OrderDAO;
import com.mobilestore.dao.CartDAO;
import com.mobilestore.entity.CartItem;
import com.mobilestore.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "VNPayServlet", urlPatterns = {"/vnpay-payment"})
public class VNPayServlet extends HttpServlet {
    
    private final OrderDAO orderDAO = new OrderDAO();
    private final CartDAO cartDAO = new CartDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=" + 
                request.getContextPath() + "/checkout");
            return;
        }
        
        HttpSession session = request.getSession();
        
        // Lấy giỏ hàng
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            cart = cartDAO.findByUserId(user.getId());
        }
        
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        
        // Tính tổng tiền
        double total = 0.0;
        for (CartItem item : cart) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        
        // Tạo order ID (sử dụng timestamp)
        String orderId = "ORDER_" + System.currentTimeMillis();
        
        // Lưu thông tin vào session để sử dụng khi return
        session.setAttribute("vnp_order_id", orderId);
        session.setAttribute("vnp_total_amount", total);
        session.setAttribute("vnp_cart", cart);
        session.setAttribute("vnp_user_id", user.getId());
        
        // Lấy IP client
        String ipAddr = getClientIP(request);
        
        // Tạo URL thanh toán
        String paymentUrl = VNPayConfig.createPaymentUrl(
            (long) total,
            orderId,
            "Thanh toan don hang " + orderId,
            ipAddr
        );
        
        response.sendRedirect(paymentUrl);
    }
    
    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

