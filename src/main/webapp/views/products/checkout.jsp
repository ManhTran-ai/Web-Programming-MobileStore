<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán - Mobile Store</title>
    <style>
        body{font-family:Arial, sans-serif;background:#fff;color:#1a1a1a}
        .container{max-width:800px;margin:2rem auto;padding:0 16px}
        table{width:100%;border-collapse:collapse}
        th,td{padding:8px;border:1px solid #ddd;text-align:left}
        .right{text-align:right}
        .btn{padding:10px 14px;border-radius:6px;background:#111;color:#fff;border:none;cursor:pointer}
    </style>
</head>
<body>
    <div class="container">
        <h1>Thanh toán</h1>
        <c:if test="${not empty error}">
            <div style="color:#c62828">${error}</div>
        </c:if>
        <c:choose>
            <c:when test="${empty cartItems}">
                <p>Giỏ hàng trống.</p>
            </c:when>
            <c:otherwise>
                <form method="post" action="${pageContext.request.contextPath}/checkout">
                    <table>
                        <thead><tr><th>Sản phẩm</th><th>Giá</th><th>Số lượng</th><th>Thành tiền</th></tr></thead>
                        <tbody>
                            <c:set var="total" value="0"/>
                            <c:forEach var="item" items="${cartItems}">
                                <tr>
                                    <td>${item.product.productName}</td>
                                    <td><fmt:formatNumber value="${item.product.price}" type="number" groupingUsed="true"/>₫</td>
                                    <td>${item.quantity}</td>
                                    <td class="right"><fmt:formatNumber value="${item.product.price * item.quantity}" type="number" groupingUsed="true"/>₫</td>
                                </tr>
                                <c:set var="total" value="${total + (item.product.price * item.quantity)}"/>
                            </c:forEach>
                        </tbody>
                    </table>
                    <p class="right">Tổng: <strong><fmt:formatNumber value="${total}" type="number" groupingUsed="true"/>₫</strong></p>
                    <div style="display:flex; gap:8px; justify-content:flex-end;">
                        <a class="btn" href="${pageContext.request.contextPath}/cart">Quay lại giỏ hàng</a>
                        <button class="btn" type="submit">Xác nhận đặt hàng</button>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>


