<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Giỏ hàng - Mobile Store</title>
    <style>
        body{font-family:Arial, sans-serif;background:#fff;color:#1a1a1a}
        .container{max-width:1000px;margin:2rem auto;padding:0 16px}
        table{width:100%;border-collapse:collapse}
        th,td{padding:12px;border:1px solid #ddd;text-align:left}
        .qty-controls {display:flex;gap:8px;align-items:center}
        .btn{padding:8px 12px;border-radius:6px;background:#111;color:#fff;text-decoration:none;border:none;cursor:pointer}
        .btn.secondary{background:#e5e5ea;color:#111}
        .right{text-align:right}
    </style>
</head>
<body>
    <div class="container">
        <!-- topbar copied from product-list for consistency -->
        <header style="background:#1a1a1a;padding:12px 0;margin-bottom:12px;">
            <div style="max-width:1200px;margin:0 auto;padding:0 24px;display:flex;justify-content:space-between;align-items:center;color:#fff">
                <div style="font-weight:600">Mobile Store</div>
                <div>
                    <a href="${pageContext.request.contextPath}/products" style="color:#fff;margin-right:12px;">Sản Phẩm</a>
                </div>
            </div>
        </header>
        <h1>Giỏ Hàng Của Bạn</h1>
        <c:choose>
            <c:when test="${empty cartItems}">
                <p>Giỏ hàng trống. <a href="${pageContext.request.contextPath}/products">Tiếp tục mua sắm</a></p>
            </c:when>
            <c:otherwise>
                <form method="post" action="${pageContext.request.contextPath}/cart?action=update">
                <table>
                    <thead>
                        <tr>
                            <th>Sản Phẩm</th>
                            <th>Giá</th>
                            <th>Số Lượng</th>
                            <th>Thành Tiền</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:set var="total" value="0" />
                        <c:forEach var="item" items="${cartItems}" varStatus="st">
                            <tr>
                                <td>
                                    <img src="${pageContext.request.contextPath}/${item.product.image}" alt="${item.product.productName}" style="height:60px;vertical-align:middle;margin-right:8px">
                                    ${item.product.productName}<br><small>${item.product.manufacturer}</small>
                                </td>
                                <td><fmt:formatNumber value="${item.product.price}" type="number" groupingUsed="true"/>₫</td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/cart?action=update" style="display:inline">
                                        <input type="hidden" name="index" value="${st.index}">
                                        <input type="number" name="quantity" value="${item.quantity}" min="1" style="width:60px">
                                        <button class="btn" type="submit">Cập nhật</button>
                                    </form>
                                </td>
                                <td class="right">
                                    <fmt:formatNumber value="${item.product.price * item.quantity}" type="number" groupingUsed="true"/>₫
                                </td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/cart?action=remove">
                                        <input type="hidden" name="index" value="${st.index}">
                                        <button class="btn secondary" type="submit">Xóa</button>
                                    </form>
                                </td>
                            </tr>
                            <c:set var="total" value="${total + (item.product.price * item.quantity)}" />
                        </c:forEach>
                    </tbody>
                </table>
                </form>

                <div style="margin-top:16px;text-align:right">
                    <p>Tạm tính: <strong><fmt:formatNumber value="${total}" type="number" groupingUsed="true"/>₫</strong></p>
                    <a class="btn secondary" href="${pageContext.request.contextPath}/products">Tiếp Tục Mua Sắm</a>
                    <a class="btn" href="${pageContext.request.contextPath}/checkout">Thanh Toán</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    <script>
        function refreshCartCountHeader() {
            fetch('${pageContext.request.contextPath}/cart/count')
                .then(r => r.json())
                .then(data => {
                    const el = document.getElementById('cartCountHeader');
                    if (el) el.textContent = data.count;
                }).catch(()=>{});
        }
        refreshCartCountHeader();
    </script>
</body>
</html>


