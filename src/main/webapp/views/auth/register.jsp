<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;
            line-height: 1.6;
            color: #1a1a1a;
            background-color: #ffffff;
        }
        .header {
            background: #1a1a1a;
            border-bottom: none;
            height: 72px;
            padding: 0;
        }
        .container { max-width: 976px; margin: 0 auto; padding: 0 24px; }
        .header-content { display: flex; justify-content: space-between; align-items: center; height:100%; }
        .logo { font-size: 1.5rem; font-weight: 600; color: #ffffff; letter-spacing: -0.5px; }
        .nav { display: flex; gap: 2rem; align-items: center; }
        .nav a { color: #ffffff; text-decoration: none; font-size: 0.95rem; font-weight: 400; transition: opacity 0.2s; display:inline-flex; align-items:center; height:72px; line-height:normal; }
        .nav a:hover { opacity: 0.7; }
        .logo { font-size: 1.5rem; font-weight: 600; color: #ffffff; letter-spacing: -0.5px; display:flex; align-items:center; height:72px; }

        .page { padding: 4rem 0; min-height: calc(100vh - 200px); }
        .center { max-width: 420px; margin: 0 auto; }
        .card {
            background: #ffffff;
            padding: 3rem 2.5rem;
            border-radius: 12px;
            border: 1px solid #e5e5e5;
        }
        h2 { 
            margin-bottom: 2rem; 
            text-align: center; 
            color: #1a1a1a; 
            font-size: 1.75rem;
            font-weight: 600;
            letter-spacing: -0.5px;
        }
        .field { margin-bottom: 1.5rem; }
        label { 
            display: block; 
            margin-bottom: 8px; 
            color: #1a1a1a; 
            font-weight: 500; 
            font-size: 0.95rem;
        }
        input { 
            width: 100%; 
            padding: 14px 16px; 
            border: 1px solid #e5e5e5; 
            border-radius: 8px; 
            font-size: 0.95rem;
            color: #1a1a1a;
            background: #ffffff;
            transition: border-color 0.2s;
        }
        input:focus { 
            outline: none; 
            border-color: #1a1a1a; 
        }
        .btn { 
            display: block; 
            width: 100%; 
            padding: 14px 16px; 
            background: #1a1a1a; 
            color: #ffffff; 
            text-align: center; 
            border: none; 
            border-radius: 8px; 
            font-weight: 500; 
            font-size: 0.95rem;
            cursor: pointer; 
            transition: background-color 0.2s, transform 0.2s; 
        }
        .btn:hover { 
            background: #333;
            transform: translateY(-1px);
        }
        .error { 
            color: #b91c1c; 
            margin-bottom: 1rem; 
            background: #fef2f2; 
            border: 1px solid #fecaca; 
            padding: 12px 16px; 
            border-radius: 8px; 
            font-size: 0.9rem;
        }
        .success { 
            color: #065f46; 
            margin-bottom: 1rem; 
            background: #f0fdf4; 
            border: 1px solid #bbf7d0; 
            padding: 12px 16px; 
            border-radius: 8px; 
            font-size: 0.9rem;
        }
        .helper { 
            margin-top: 1.5rem; 
            text-align: center; 
            font-size: 0.9rem;
            color: #666;
        }
        .helper a { 
            color: #1a1a1a; 
            text-decoration: none; 
            font-weight: 500;
        }
        .helper a:hover { 
            text-decoration: underline; 
        }
        .password-hint { 
            font-size: 0.85rem; 
            color: #666; 
            margin-top: 6px; 
        }
    </style>
</head>
<body>
    <!-- Header -->
<header class="header">
    <div class="container">
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
                        <a href="${pageContext.request.contextPath}/register" style="color:#fff; font-weight:600;">Đăng Ký</a>
                        <a href="${pageContext.request.contextPath}/login">Đăng Nhập</a>
                    </c:otherwise>
                </c:choose>
            </nav>
        </div>
    </div>
</header>

    <!-- Page Content -->
    <section class="page">
        <div class="container">
            <div class="center">
                <div class="card">
                    <h2>Đăng ký tài khoản</h2>
                    <c:if test="${not empty error}">
                        <div class="error">${error}</div>
                    </c:if>
                    <c:if test="${not empty success}">
                        <div class="success">${success}</div>
                    </c:if>
                    <form method="post" action="${pageContext.request.contextPath}/register">
                        <div class="field">
                            <label for="username">Tên đăng nhập</label>
                            <input type="text" id="username" name="username" required 
                                   value="${param.username}" />
                        </div>
                        <div class="field">
                            <label for="password">Mật khẩu</label>
                            <input type="password" id="password" name="password" required />
                            <div class="password-hint">Mật khẩu phải có ít nhất 6 ký tự</div>
                        </div>
                        <div class="field">
                            <label for="confirmPassword">Xác nhận mật khẩu</label>
                            <input type="password" id="confirmPassword" name="confirmPassword" required />
                        </div>
                        <button class="btn" type="submit">Đăng ký</button>
                    </form>
                    <div class="helper">
                        Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a>
                    </div>
                    <div class="helper">
                        <a href="${pageContext.request.contextPath}/">Quay về trang chủ</a>
                    </div>
                </div>
            </div>
        </div>
    </section>
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

