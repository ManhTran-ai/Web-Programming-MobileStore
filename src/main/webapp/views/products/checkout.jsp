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
            max-width: 800px;
            margin: 2rem auto;
            padding: 0 16px;
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
            max-width: 976px;
            margin: 0 auto;
            padding: 0 24px;
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

        table{width:100%;border-collapse:collapse;border-radius:8px;overflow:hidden;margin-bottom:2rem}
        th,td{padding:12px;border:1px solid #e5e5ea;text-align:left}
        th{background:#f8f9fa;font-weight:600}
        .right{text-align:right}
        .btn{padding:10px 14px;border-radius:6px;background:#111;color:#fff;border:none;cursor:pointer;text-decoration:none;display:inline-block}
        .btn:hover{background:#333}

        @media (max-width: 768px) {
            .header-content {
                padding: 0 12px;
            }
            .nav {
                gap: 1rem;
            }
            .nav a {
                font-size: 0.9rem;
            }

            .container {
                padding: 0 12px;
            }

            main.container {
                padding-top: 80px;
            }

            table {
                font-size: 14px;
            }

            th, td {
                padding: 8px 4px;
            }

            .btn {
                padding: 8px 12px;
                font-size: 14px;
            }

            h1 {
                font-size: 1.5rem !important;
            }
        }
    </style>
</head>
<body>
<header class="header">
    <div class="header-content">
        <div class="logo">Mobile Store</div>
        <nav class="nav">
            <a href="${pageContext.request.contextPath}/">Trang Chủ</a>
            <a href="${pageContext.request.contextPath}/products">Sản Phẩm</a>
            <a href="${pageContext.request.contextPath}/cart">Giỏ Hàng(<span id="cartCount">0</span>)</a>
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
</header>

<main class="container" style="padding-top: 100px;">
    <div style="padding: 2rem 0;">
        <h1 style="font-size: 2rem; font-weight: 600; margin-bottom: 2rem;">Thanh toán</h1>

        <c:if test="${not empty error}">
            <div style="color:#c62828; background: #fdecea; padding: 1rem; border-radius: 8px; margin-bottom: 2rem; border: 1px solid #f5c6cb;">
                <strong>❌ Lỗi:</strong> ${error}
            </div>
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
                    <div style="background:#f8f9fa;padding:1.5rem;border-radius:8px;margin-top:1rem;">
                        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:1rem;">
                            <span style="font-size:1.2rem;font-weight:600;">Tổng cộng:</span>
                            <span style="font-size:1.5rem;font-weight:700;color:#0071e3;">
                                <fmt:formatNumber value="${total}" type="number" groupingUsed="true"/>₫
                            </span>
                        </div>
                        <div style="display:flex; gap:12px; justify-content:flex-end;">
                            <a class="btn" href="${pageContext.request.contextPath}/cart" style="background:#e5e5ea;color:#1a1a1a;">Quay lại giỏ hàng</a>
                            <button class="btn" type="submit">Xác nhận đặt hàng</button>
                        </div>
                    </div>
                </form>
            </c:otherwise>
        </c:choose>
    </div>
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


