package com.mobilestore.controller;

import com.mobilestore.dao.ProductDAO;
import com.mobilestore.entity.CartItem;
import com.mobilestore.entity.Product;
import com.mobilestore.entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart", "/cart/*"})
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final ProductDAO productDAO = new ProductDAO();
    private final com.mobilestore.dao.CartDAO cartDAO = new com.mobilestore.dao.CartDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Require login
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            // redirect to login with return url
            response.sendRedirect(request.getContextPath() + "/login?redirect=" + request.getRequestURI());
            return;
        }

        // Load cart from DB for user if session cart empty
        List<CartItem> cart = null;
        Object cartObj = request.getSession().getAttribute("cart");
        if (cartObj instanceof List) {
            cart = (List<CartItem>) cartObj;
        }
        if (cart == null) {
            cart = cartDAO.findByUserId(user.getId());
            request.getSession().setAttribute("cart", cart);
        }

        request.setAttribute("cartItems", cart);
        request.getRequestDispatcher("/views/products/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Actions: add, remove, update
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            String loginUrl = request.getContextPath() + "/login?redirect=" + request.getContextPath() + "/cart";
            String xreq = request.getHeader("X-Requested-With");
            if (xreq != null && "XMLHttpRequest".equalsIgnoreCase(xreq)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().print("{\"redirect\":\"" + loginUrl + "\"}");
                return;
            } else {
                response.sendRedirect(loginUrl);
                return;
            }
        }

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String idStr = request.getParameter("productId");
            String qtyStr = request.getParameter("quantity");
            int quantity = 1;
            try { quantity = Math.max(1, Integer.parseInt(qtyStr)); } catch (Exception ignored) {}
            try {
                int productId = Integer.parseInt(idStr);
                Product product = productDAO.findById(productId);
                if (product != null) {
                    List<CartItem> cart = null;
                    Object cartObj2 = request.getSession().getAttribute("cart");
                    if (cartObj2 instanceof List) {
                        cart = (List<CartItem>) cartObj2;
                    }
                    if (cart == null) {
                        cart = new ArrayList<>();
                        request.getSession().setAttribute("cart", cart);
                    }
                    // check existing and compute new total quantity
                    boolean found = false;
                    int currentQty = 0;
                    for (CartItem item : cart) {
                        if (item.getProduct().getId().equals(product.getId())) {
                            currentQty = item.getQuantity();
                            found = true;
                            break;
                        }
                    }
                    int newQuantity = currentQty + quantity;

                    // Validate stock
                    if (product.getQuantityInStock() < newQuantity) {
                        String msg;
                        if (product.getQuantityInStock() <= 0) {
                            msg = "Sản phẩm đã hết hàng";
                        } else {
                            msg = "Chỉ còn " + product.getQuantityInStock() + " sản phẩm trong kho";
                        }
                        String xreq = request.getHeader("X-Requested-With");
                        if (xreq != null && "XMLHttpRequest".equalsIgnoreCase(xreq)) {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().print("{\"success\":false,\"message\":\"" + msg + "\"}");
                            return;
                        } else {
                            // redirect back with error parameter (could be improved to flash)
                            response.sendRedirect(request.getContextPath() + "/products?error=" + java.net.URLEncoder.encode(msg, "UTF-8"));
                            return;
                        }
                    }

                    // apply to session cart
                    if (found) {
                        for (CartItem item : cart) {
                            if (item.getProduct().getId().equals(product.getId())) {
                                item.setQuantity(newQuantity);
                                break;
                            }
                        }
                    } else {
                        cart.add(new CartItem(product, newQuantity));
                    }

                    // persist to DB with the new total quantity
                    cartDAO.upsertCartItem(user.getId(), product.getId(), newQuantity);

                    // compute total quantity for response
                    int totalQty = 0;
                    for (CartItem it : cart) totalQty += it.getQuantity();

                    // If AJAX request, return JSON; otherwise redirect
                    String xreq = request.getHeader("X-Requested-With");
                    if (xreq != null && "XMLHttpRequest".equalsIgnoreCase(xreq)) {
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().print("{\"success\":true, \"count\":" + totalQty + "}");
                        return;
                    }
                }
                response.sendRedirect(request.getContextPath() + "/cart");
                return;
            } catch (Exception e) {
                // Log and return error
                e.printStackTrace();
                String xreq = request.getHeader("X-Requested-With");
                if (xreq != null && "XMLHttpRequest".equalsIgnoreCase(xreq)) {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().print("{\"success\":false, \"message\":\"" + e.getMessage() + "\"}");
                    return;
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi thêm vào giỏ");
                    return;
                }
            }
        } else if ("remove".equals(action)) {
            String idxStr = request.getParameter("index");
            try {
                int idx = Integer.parseInt(idxStr);
                List<CartItem> cart = null;
                Object cartObj3 = request.getSession().getAttribute("cart");
                if (cartObj3 instanceof List) {
                    cart = (List<CartItem>) cartObj3;
                }
                if (cart != null && idx >=0 && idx < cart.size()) {
                    int productId = cart.get(idx).getProduct().getId();
                    cart.remove(idx);
                    cartDAO.deleteCartItem(user.getId(), productId);
                }
            } catch (Exception ignored) {}
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        } else if ("update".equals(action)) {
            String idxStr = request.getParameter("index");
            String qtyStr = request.getParameter("quantity");
            try {
                int idx = Integer.parseInt(idxStr);
                int qty = Math.max(1, Integer.parseInt(qtyStr));
                List<CartItem> cart = null;
                Object cartObj4 = request.getSession().getAttribute("cart");
                if (cartObj4 instanceof List) {
                    cart = (List<CartItem>) cartObj4;
                }
                if (cart != null && idx >=0 && idx < cart.size()) {
                    int productId = cart.get(idx).getProduct().getId();
                    Product fresh = productDAO.findById(productId);
                    if (fresh != null && fresh.getQuantityInStock() < qty) {
                        String msg;
                        if (fresh.getQuantityInStock() <= 0) msg = "Sản phẩm đã hết hàng";
                        else msg = "Chỉ còn " + fresh.getQuantityInStock() + " sản phẩm trong kho";
                        String xreq = request.getHeader("X-Requested-With");
                        if (xreq != null && "XMLHttpRequest".equalsIgnoreCase(xreq)) {
                            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().print("{\"success\":false,\"message\":\"" + msg + "\"}");
                            return;
                        } else {
                            response.sendRedirect(request.getContextPath() + "/cart?error=" + java.net.URLEncoder.encode(msg, "UTF-8"));
                            return;
                        }
                    }
                    cart.get(idx).setQuantity(qty);
                    cartDAO.upsertCartItem(user.getId(), productId, qty);
                }
            } catch (Exception ignored) {}
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}


