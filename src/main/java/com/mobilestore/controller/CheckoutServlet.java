package com.mobilestore.controller;

import com.mobilestore.dao.OrderDAO;
import com.mobilestore.dao.CartDAO;
import com.mobilestore.entity.CartItem;
import com.mobilestore.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final OrderDAO orderDAO = new OrderDAO();
    private final CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=" + request.getRequestURI());
            return;
        }
        
        List<CartItem> cart = null;
        Object cartObj = request.getSession().getAttribute("cart");
        if (cartObj instanceof List) {
            cart = (List<CartItem>) cartObj;
        }
        
        if (cart == null || cart.isEmpty()) {
            cart = cartDAO.findByUserId(user.getId());
            if (cart != null && !cart.isEmpty()) {
                request.getSession().setAttribute("cart", cart);
            }
        }
        
        if (cart == null) {
            cart = new java.util.ArrayList<>();
        }
        
        request.setAttribute("cartItems", cart);
        request.getRequestDispatcher("/views/products/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?redirect=" + request.getContextPath() + "/checkout");
            return;
        }

        List<CartItem> cart = null;
        Object cartObj = request.getSession().getAttribute("cart");
        if (cartObj instanceof List) {
            cart = (List<CartItem>) cartObj;
        }
        
        if (cart == null || cart.isEmpty()) {
            cart = cartDAO.findByUserId(user.getId());
        }
        
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?error=Giỏ hàng trống");
            return;
        }
        
        double total = 0.0;
        for (CartItem it : cart) total += it.getProduct().getPrice() * it.getQuantity();

        Integer orderId = orderDAO.createOrder(user.getId(), total, cart);
        if (orderId != null) {
            request.getSession().removeAttribute("cart");
            cartDAO.clearCartByUser(user.getId());
            response.sendRedirect(request.getContextPath() + "/products?success=order_placed&id=" + orderId);
        } else {
            request.setAttribute("error", "Không thể tạo đơn hàng. Vui lòng thử lại.");
            doGet(request, response);
        }
    }
}
