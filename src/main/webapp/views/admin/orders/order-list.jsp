<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Đơn hàng - Trang quản lý</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', 'Helvetica Neue', Arial, sans-serif;
            line-height: 1.6;
            color: #1a1a1a;
            background-color: #f5f5f7;
        }

        .admin-container {
            display: flex;
            min-height: 100vh;
        }

        /* Sidebar */
        .sidebar {
            width: 260px;
            background: #1a1a1a;
            color: #ffffff;
            padding: 2rem 0;
            position: fixed;
            height: 100vh;
            overflow-y: auto;
        }

        .sidebar-header {
            padding: 0 1.5rem 2rem;
            border-bottom: 1px solid #333;
            margin-bottom: 1rem;
        }

        .sidebar-header h2 {
            font-size: 1.25rem;
            font-weight: 600;
            color: #ffffff;
        }

        .sidebar-header span {
            font-size: 0.875rem;
            color: #888;
        }

        .sidebar-nav {
            list-style: none;
        }

        .sidebar-nav li {
            margin: 0.25rem 0;
        }

        .sidebar-nav a {
            display: flex;
            align-items: center;
            padding: 0.875rem 1.5rem;
            color: #ccc;
            text-decoration: none;
            transition: all 0.2s;
            font-size: 0.95rem;
        }

        .sidebar-nav a:hover,
        .sidebar-nav a.active {
            background: #333;
            color: #ffffff;
        }

        .sidebar-nav a.active {
            border-left: 3px solid #0071e3;
        }

        .sidebar-nav .icon {
            margin-right: 0.75rem;
            font-size: 1.1rem;
        }

        /* Main Content */
        .main-content {
            flex: 1;
            margin-left: 260px;
            padding: 2rem;
        }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2rem;
        }

        .page-header h1 {
            font-size: 1.75rem;
            font-weight: 600;
            color: #1a1a1a;
        }

        /* Table */
        table {
            width: 100%;
            border-collapse: collapse;
            background: #fff;
        }

        th, td {
            padding: 10px;
            border: 1px solid #eee;
        }

        .actions a {
            margin-right: 8px;
        }

        @media (max-width: 768px) {
            .sidebar {
                display: none;
            }

            .main-content {
                margin-left: 0;
            }
        }
    </style>
</head>

<body>
    <div class="admin-container">
        <!-- Sidebar -->
        <aside class="sidebar">
            <div class="sidebar-header">
                <h2>Mobile Store</h2>
                <span>Trang quản lý</span>
            </div>
            <nav>
                <ul class="sidebar-nav">
                    <li>
                        <a href="${pageContext.request.contextPath}/">
                            Trang chủ
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/products">
                            Sản phẩm
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/admin/orders" class="active">
                            Đơn hàng
                        </a>
                    </li>
                </ul>
            </nav>
        </aside>

        <!-- Main Content -->
        <main class="main-content">
            <div class="page-header">
                <h1>Quản lý Đơn hàng</h1>
            </div>

            <div class="container">
                <c:if test="${not empty orders}">
                    <table>
                        <thead><tr><th>ID</th><th>Khách hàng</th><th>Ngày</th><th>Tổng</th><th>Trạng thái</th><th>Hành động</th></tr></thead>
                        <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td>${o.id}</td>
                                <td>${o.user != null ? o.user.username : 'Khách'}</td>
                                <td><fmt:formatDate value="${o.orderDate}" pattern="yyyy-MM-dd HH:mm"/></td>
                                <td><fmt:formatNumber value="${o.totalAmount}" type="number" groupingUsed="true"/>₫</td>
                                <td>${o.orderStatus}</td>
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/admin/orders/view?id=${o.id}">Xem</a>
                                    <form method="post" action="${pageContext.request.contextPath}/admin/orders/delete" style="display:inline">
                                        <input type="hidden" name="id" value="${o.id}"/>
                                        <button type="submit" onclick="return confirm('Xác nhận xóa?')">Xóa</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:if>
                <c:if test="${empty orders}">
                    <p>Chưa có đơn hàng nào.</p>
                </c:if>
            </div>
        </main>
    </div>
</body>
</html>


