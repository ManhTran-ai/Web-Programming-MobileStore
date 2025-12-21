<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            line-height: 1.6;
            color: #333;
            background-color: #f5f5f5;
        }
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 1rem 0;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .container { max-width: 1200px; margin: 0 auto; padding: 0 20px; }
        .header-content { display: flex; justify-content: space-between; align-items: center; }
        .logo { font-size: 1.8rem; font-weight: bold; }
        .nav { display: flex; gap: 2rem; }
        .nav a { color: white; text-decoration: none; transition: opacity 0.3s; }
        .nav a:hover { opacity: 0.8; }

        .page { padding: 3rem 0; }
        .center { max-width: 420px; margin: 0 auto; }
        .card {
            background: white;
            padding: 2rem;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0,0,0,0.1);
        }
        h2 { margin-bottom: 1rem; text-align: center; color: #111827; }
        .field { margin-bottom: 1rem; }
        label { display:block; margin-bottom: 6px; color:#374151; font-weight: 600; }
        input { width:100%; padding:12px 14px; border:1px solid #d1d5db; border-radius:8px; }
        input:focus { outline: none; border-color: #667eea; }
        .btn { display:block; width:100%; padding:12px 16px; background:#667eea; color:#fff; text-align:center; border:none; border-radius:8px; font-weight:600; cursor:pointer; transition: transform .2s; }
        .btn:hover { transform: translateY(-1px); }
        .error { color:#b91c1c; margin-bottom: 12px; background:#fee2e2; border:1px solid #fecaca; padding:10px 12px; border-radius:8px; }
        .success { color:#065f46; margin-bottom: 12px; background:#d1fae5; border:1px solid #a7f3d0; padding:10px 12px; border-radius:8px; }
        .helper { margin-top: 12px; text-align: center; }
        .helper a { color:#667eea; text-decoration: none; }
        .helper a:hover { text-decoration: underline; }
        .password-hint { font-size: 0.875rem; color: #6b7280; margin-top: 4px; }
    </style>
</head>
<body>
    <!-- Header -->
    <header class="header">
        <div class="container">
            <div class="header-content">
                <div class="logo">📱 Mobile Store</div>
                <nav class="nav">
                    <a href="${pageContext.request.contextPath}/">Trang Chủ</a>
                    <a href="${pageContext.request.contextPath}/products">Sản Phẩm</a>
                    <a href="${pageContext.request.contextPath}/cart">Giỏ Hàng</a>
                    <a href="${pageContext.request.contextPath}/login">Đăng Nhập</a>
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
</body>
</html>

