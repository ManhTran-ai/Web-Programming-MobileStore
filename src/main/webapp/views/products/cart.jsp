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
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial;
            background: #fff;
            color: #1a1a1a;
        }

        .container {
            max-width: 976px;
            margin: 0 auto;
            padding: 0 24px;
        }

        .header {
            background: #1a1a1a;
            border-bottom: none;
            height: 72px;
            padding: 0;
            position: sticky;
            top: 0;
            z-index: 100;
        }

        .header-content {
            display: flex;
            justify-content: space-between;
            align-items: center;
            height: 100%;
        }

        .logo {
            font-size: 1.5rem;
            font-weight: 600;
            color: #ffffff;
            letter-spacing: -0.5px;
            display: flex;
            align-items: center;
            height: 72px;
        }

        .nav {
            display: flex;
            gap: 2rem;
            align-items: center;
        }

        .nav a {
            color: #ffffff;
            text-decoration: none;
            font-size: 0.95rem;
            font-weight: 400;
            transition: opacity 0.2s;
            display: inline-flex;
            align-items: center;
            height: 72px;
            line-height: normal;
        }

        .nav a:hover {
            opacity: 0.7;
        }

        table{width:100%;border-collapse:collapse}
        th,td{padding:12px;border:1px solid #ddd;text-align:left}
        .qty-controls {display:flex;gap:8px;align-items:center}
        .btn{padding:8px 12px;border-radius:6px;background:#111;color:#fff;text-decoration:none;border:none;cursor:pointer}
        .btn.secondary{background:#e5e5ea;color:#111}
        .right{text-align:right}

        @media (max-width: 768px) {
            .container {
                padding: 0 12px;
            }
        }
    </style>
</head>
<body>
<header class="header">
    <div class="container">
        <div class="header-content">
            <div class="logo">Mobile Store</div>
            <nav class="nav">
                <a href="${pageContext.request.contextPath}/">Trang Chủ</a>
                <a href="${pageContext.request.contextPath}/products">Sản Phẩm</a>
                <a href="${pageContext.request.contextPath}/cart" style="color:#fff; font-weight:600;">Giỏ Hàng(<span id="cartCount">0</span>)</a>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <c:if test="${sessionScope.user.role.name == 'ADMIN'}">
                            <a href="${pageContext.request.contextPath}/admin/products" style="color:#0071e3;">Trang
                                Quản Lý</a>
                        </c:if>
                        <span style="color:#ccc;">Xin chào, ${sessionScope.user.username}</span>
                        <a href="${pageContext.request.contextPath}/logout">Đăng Xuất</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/register">Đăng Ký</a>
                        <a href="${pageContext.request.contextPath}/login">Đăng Nhập</a>
                    </c:otherwise>
                </c:choose>
            </nav>
        </div>
    </div>
</header>

    <main class="container">
        <div style="padding: 2rem 0;">
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
    </main>
    <script>
        function refreshCartCount() {
            fetch('${pageContext.request.contextPath}/cart/count')
                .then(r => r.json())
                .then(data => {
                    const el = document.getElementById('cartCount');
                    if (el) el.textContent = data.count;
                }).catch(() => {});
        }

        // init
        refreshCartCount();
    </script>
</body>
</html>


