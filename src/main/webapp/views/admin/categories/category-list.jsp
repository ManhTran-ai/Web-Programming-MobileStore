<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh mục - Admin Panel</title>
    <style>
        /* Shared admin panel styles (kept consistent with product form) */
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif; line-height:1.6; color:#1a1a1a; background-color:#f5f5f7; }
        .admin-container { display:flex; min-height:100vh; }
        .sidebar { width:260px; background:#1a1a1a; color:#fff; padding:2rem 0; position:fixed; height:100vh; overflow-y:auto; }
        .sidebar-header { padding:0 1.5rem 2rem; border-bottom:1px solid #333; margin-bottom:1rem; }
        .sidebar-header h2 { font-size:1.25rem; font-weight:600; color:#fff; }
        .sidebar-nav { list-style:none; }
        .sidebar-nav a { display:flex; align-items:center; padding:0.875rem 1.5rem; color:#ccc; text-decoration:none; transition:all .2s; font-size:0.95rem; }
        .sidebar-nav a.active, .sidebar-nav a:hover { background:#333; color:#fff; }
        .main-content { flex:1; margin-left:260px; padding:2rem; }
        .page-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:1.25rem; }
        .page-header h1 { font-size:1.5rem; font-weight:600; color:#1a1a1a; }
        .card { background:#fff; border-radius:12px; box-shadow:0 1px 3px rgba(0,0,0,0.1); padding:1.25rem; }
        table { width:100%; border-collapse:collapse; margin-top:1rem; }
        th, td { padding:0.75rem; text-align:left; border-bottom:1px solid #eee; }
        .actions a { margin-right:0.5rem; text-decoration:none; color:#0071e3; }
        .btn { display:inline-flex; align-items:center; justify-content:center; padding:0.5rem 0.875rem; border-radius:8px; font-size:0.95rem; font-weight:500; text-decoration:none; color:#fff; background:#0071e3; border:none; cursor:pointer; }
        .btn-secondary { background:#e5e5ea; color:#1a1a1a; }
        .alert { padding:0.75rem 1rem; border-radius:8px; margin-bottom:1rem; font-size:0.95rem; }
        .alert-error { background:#fdecea; color:#c62828; border:1px solid #f5c6cb; }
        .alert-success { background:#e6ffed; color:#087f3b; border:1px solid #cdeed4; }
        @media (max-width:768px) { .main-content { margin-left:0; padding:1rem; } .sidebar { display:none; } }
    </style>
</head>
<body>
    <div class="admin-container">
        <!-- Sidebar (same as other admin pages) -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <h2>Mobile Store</h2>
                <span>Admin Panel</span>
            </div>
            <nav>
                <ul class="sidebar-nav">
                    <li><a href="${pageContext.request.contextPath}/"><span class="icon">🏠</span>&nbsp;Trang chủ</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/products" class="active"><span class="icon">📱</span>&nbsp;Sản phẩm</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/orders"><span class="icon">📦</span>&nbsp;Đơn hàng</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/users"><span class="icon">👥</span>&nbsp;Người dùng</a></li>
                </ul>
            </nav>
        </aside>

        <!-- Main Content -->
        <main class="main-content">
            <div class="page-header">
                <h1>Danh mục</h1>
                <div>
                    <!-- Keep ability to add category via product form; provide quick link to add product -->
                    <a class="btn" href="${pageContext.request.contextPath}/admin/products/add">Thêm sản phẩm</a>
                </div>
            </div>

            <c:if test="${not empty param.error}">
                <div class="alert alert-error">Lỗi: ${param.error}</div>
            </c:if>
            <c:if test="${not empty param.success}">
                <div class="alert alert-success">Hoàn tất</div>
            </c:if>

            <div class="card">
                <table>
                    <thead>
                        <tr>
                            <th style="width:80px">#</th>
                            <th>Tên danh mục</th>
                            <th style="width:200px">Hành động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="cat" items="${categories}">
                            <tr>
                                <td>${cat.id}</td>
                                <td>${cat.name}</td>
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/admin/categories/edit?id=${cat.id}">Sửa</a>
                                    <a href="${pageContext.request.contextPath}/admin/categories/delete?id=${cat.id}&confirm=true"
                                       onclick="return confirm('Xác nhận xóa danh mục này?')">Xóa</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty categories}">
                            <tr><td colspan="3">Chưa có danh mục nào.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</body>
</html>


