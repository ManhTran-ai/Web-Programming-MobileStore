<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang Chủ - Mobile Store</title>
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
            background-color: #ffffff;
        }
        
        .header {
            background: #1a1a1a;
            border-bottom: none;
            padding: 1.25rem 0;
            position: sticky;
            top: 0;
            z-index: 100;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 24px;
        }
        
        .header-content {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .logo {
            font-size: 1.5rem;
            font-weight: 600;
            color: #ffffff;
            letter-spacing: -0.5px;
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
        }
        
        .nav a:hover {
            opacity: 0.7;
        }
        
        .hero {
            background: #ffffff;
            padding: 0;
            position: relative;
            overflow: hidden;
        }
        
        .carousel-container {
            position: relative;
            width: 100%;
            max-width: 1400px;
            margin: 0 auto;
            overflow: hidden;
        }
        
        .carousel-slides {
            display: flex;
            transition: transform 0.6s ease-in-out;
            will-change: transform;
            position: relative;
        }
        
        .carousel-slide {
            min-width: 100%;
            width: 100%;
            flex: 0 0 100%;
            flex-shrink: 0;
            position: relative;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #ffffff;
            padding: 4rem 2rem;
        }
        
        .carousel-slide img {
            max-width: 100%;
            height: auto;
            max-height: 600px;
            object-fit: contain;
        }
        
        .carousel-nav {
            position: absolute;
            top: 50%;
            transform: translateY(-50%);
            background: rgba(26, 26, 26, 0.7);
            color: #ffffff;
            border: none;
            width: 48px;
            height: 48px;
            border-radius: 50%;
            cursor: pointer;
            font-size: 1.5rem;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: background 0.2s;
            z-index: 10;
        }
        
        .carousel-nav:hover {
            background: rgba(26, 26, 26, 0.9);
        }
        
        .carousel-nav.prev {
            left: 24px;
        }
        
        .carousel-nav.next {
            right: 24px;
        }
        
        .carousel-dots {
            display: flex;
            justify-content: center;
            gap: 12px;
            padding: 2rem 0;
            position: absolute;
            bottom: 20px;
            left: 50%;
            transform: translateX(-50%);
            z-index: 10;
        }
        
        .carousel-dot {
            width: 10px;
            height: 10px;
            border-radius: 50%;
            background: rgba(26, 26, 26, 0.3);
            border: none;
            cursor: pointer;
            transition: background 0.3s, transform 0.3s;
        }
        
        .carousel-dot.active {
            background: #1a1a1a;
            transform: scale(1.2);
        }
        
        .hero-content {
            text-align: center;
            padding: 3rem 0;
        }
        
        .hero-content h1 {
            font-size: 2.5rem;
            margin-bottom: 1rem;
            color: #1a1a1a;
            font-weight: 600;
            letter-spacing: -1px;
        }
        
        .hero-content p {
            font-size: 1.1rem;
            margin-bottom: 2.5rem;
            color: #666;
            font-weight: 400;
        }
        
        .btn {
            display: inline-block;
            padding: 14px 32px;
            background: #1a1a1a;
            color: #ffffff;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 500;
            font-size: 0.95rem;
            transition: background-color 0.2s, transform 0.2s;
            border: none;
            cursor: pointer;
        }
        
        .btn:hover {
            background: #333;
            transform: translateY(-1px);
        }
        
        .features {
            padding: 5rem 0;
            background: #ffffff;
        }
        
        .features h2 {
            text-align: center;
            margin-bottom: 3rem;
            font-size: 2rem;
            font-weight: 600;
            color: #1a1a1a;
            letter-spacing: -0.5px;
        }
        
        .features-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(260px, 1fr));
            gap: 2rem;
        }
        
        .feature-card {
            text-align: center;
            padding: 2.5rem 2rem;
            border-radius: 12px;
            border: 1px solid #e5e5e5;
            background: #ffffff;
            transition: border-color 0.2s, transform 0.2s;
        }
        
        .feature-card:hover {
            border-color: #1a1a1a;
            transform: translateY(-2px);
        }
        
        .feature-icon {
            font-size: 2.5rem;
            margin-bottom: 1.25rem;
        }
        
        .feature-card h3 {
            font-size: 1.25rem;
            margin-bottom: 0.75rem;
            color: #1a1a1a;
            font-weight: 600;
        }
        
        .feature-card p {
            color: #666;
            font-size: 0.95rem;
            line-height: 1.6;
        }
        
        .footer {
            background: #ffffff;
            color: #666;
            text-align: center;
            padding: 3rem 0;
            margin-top: 4rem;
            border-top: 1px solid #e5e5e5;
        }
        
        .footer p {
            margin-bottom: 0.5rem;
            font-size: 0.9rem;
        }
        
        .flash-message {
            max-width: 1200px;
            margin: 20px auto;
            padding: 14px 20px;
            background: #f0f9ff;
            color: #065f46;
            border: 1px solid #bae6fd;
            border-radius: 8px;
            font-size: 0.95rem;
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
                    <a href="${pageContext.request.contextPath}/cart">Giỏ Hàng</a>
                    <c:choose>
                        <c:when test="${not empty sessionScope.user}">
                            <c:if test="${sessionScope.user.role.name == 'ADMIN'}">
                                <a href="${pageContext.request.contextPath}/admin/products" style="color: #0071e3;">Admin Panel</a>
                            </c:if>
                            <span style="color: #888;">Xin chào, ${sessionScope.user.username}</span>
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

    <!-- Flash Success Message -->
    <c:if test="${not empty sessionScope.flashSuccess}">
        <div class="flash-message">
            ${sessionScope.flashSuccess}
        </div>
        <c:remove var="flashSuccess" scope="session"/>
    </c:if>

    <!-- Hero Section with Carousel -->
    <section class="hero">
        <div class="carousel-container">
            <div class="carousel-slides" id="carouselSlides">
                <div class="carousel-slide">
                    <img src="${pageContext.request.contextPath}/images/img_1.png" alt="iPhone 15 Pro" />
                </div>
                <div class="carousel-slide">
                    <img src="${pageContext.request.contextPath}/images/img_2.png" alt="iPhone 15 Pro" />
                </div>
                <div class="carousel-slide">
                    <img src="${pageContext.request.contextPath}/images/img_3.png" alt="iPhone 14 Pro" />
                </div>
            </div>
            <button class="carousel-nav prev" type="button">‹</button>
            <button class="carousel-nav next" type="button">›</button>
            <div class="carousel-dots" id="carouselDots"></div>
        </div>
        <div class="hero-content">
            <div class="container">
                <h1>Chào Mừng Đến Mobile Store</h1>
                <p>Nơi bạn tìm thấy những chiếc điện thoại tốt nhất với giá cả hợp lý</p>
                <a href="${pageContext.request.contextPath}/products" class="btn">Xem Sản Phẩm</a>
            </div>
        </div>
    </section>
    
    <script>
        // Carousel state - đặt ngoài để có thể truy cập từ mọi nơi
        let carouselState = {
            currentSlide: 0,
            totalSlides: 0,
            autoPlayInterval: null
        };
        
        // Định nghĩa các hàm trước để có thể sử dụng trong initCarousel
        function updateCarousel() {
            const slidesContainer = document.getElementById('carouselSlides');
            if (slidesContainer) {
                // Tính toán dựa trên width của slide đầu tiên (mỗi slide = 100% của parent)
                const firstSlide = slidesContainer.querySelector('.carousel-slide');
                const parentContainer = slidesContainer.parentElement;
                
                if (firstSlide && parentContainer) {
                    // Lấy width của parent container (mỗi slide = 100% của parent)
                    const slideWidth = parentContainer.offsetWidth;
                    const translateValue = -(carouselState.currentSlide * slideWidth);
                    
                    // Áp dụng transform với pixel
                    slidesContainer.style.transform = `translateX(${translateValue}px)`;
                    slidesContainer.style.webkitTransform = `translateX(${translateValue}px)`;
                    slidesContainer.style.MozTransform = `translateX(${translateValue}px)`;
                    slidesContainer.style.msTransform = `translateX(${translateValue}px)`;
                    slidesContainer.style.OTransform = `translateX(${translateValue}px)`;
                    
                    // Force reflow
                    void slidesContainer.offsetWidth;
                    
                    // Kiểm tra transform thực tế
                    const computedTransform = window.getComputedStyle(slidesContainer).transform;
                    const computedWebkitTransform = window.getComputedStyle(slidesContainer).webkitTransform;
                    
                    console.log('Carousel updated to slide:', carouselState.currentSlide, 
                               'Slide width:', slideWidth,
                               'Transform:', translateValue + 'px',
                               'Computed transform:', computedTransform || computedWebkitTransform);
                } else {
                    console.error('Không tìm thấy slide hoặc parent container');
                }
            } else {
                console.error('Không tìm thấy carouselSlides container');
            }
            
            // Update dots
            const dots = document.querySelectorAll('.carousel-dot');
            dots.forEach((dot, index) => {
                if (index === carouselState.currentSlide) {
                    dot.classList.add('active');
                } else {
                    dot.classList.remove('active');
                }
            });
        }
        
        // Định nghĩa hàm trong global scope
        window.changeSlide = function(direction) {
            if (carouselState.totalSlides === 0) {
                console.error('Carousel chưa được khởi tạo');
                return;
            }
            carouselState.currentSlide = (carouselState.currentSlide + direction + carouselState.totalSlides) % carouselState.totalSlides;
            console.log('Changing slide:', direction, 'New slide:', carouselState.currentSlide);
            updateCarousel();
        };
        
        window.goToSlide = function(index) {
            if (carouselState.totalSlides === 0) {
                console.error('Carousel chưa được khởi tạo');
                return;
            }
            if (index >= 0 && index < carouselState.totalSlides) {
                carouselState.currentSlide = index;
                console.log('Going to slide:', index);
                updateCarousel();
            }
        };
        
        function startAutoPlay() {
            stopAutoPlay(); // Clear existing interval
            carouselState.autoPlayInterval = setInterval(() => {
                window.changeSlide(1);
            }, 5000);
        }
        
        function stopAutoPlay() {
            if (carouselState.autoPlayInterval) {
                clearInterval(carouselState.autoPlayInterval);
                carouselState.autoPlayInterval = null;
            }
        }
        
        // Hàm khởi tạo carousel
        function initCarousel() {
            console.log('Initializing carousel...');
            const slides = document.querySelectorAll('.carousel-slide');
            carouselState.totalSlides = slides.length;
            console.log('Found slides:', carouselState.totalSlides);
            
            if (carouselState.totalSlides === 0) {
                console.error('Không tìm thấy slides trong carousel');
                return;
            }
            
            // Tạo dots
            const dotsContainer = document.getElementById('carouselDots');
            if (dotsContainer) {
                dotsContainer.innerHTML = ''; // Clear existing dots
                for (let i = 0; i < carouselState.totalSlides; i++) {
                    const dot = document.createElement('button');
                    dot.className = 'carousel-dot' + (i === 0 ? ' active' : '');
                    dot.setAttribute('data-slide', i);
                    dot.addEventListener('click', function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        const slideIndex = parseInt(this.getAttribute('data-slide'));
                        console.log('Dot clicked, going to slide:', slideIndex);
                        window.goToSlide(slideIndex);
                    });
                    dotsContainer.appendChild(dot);
                }
            }
            
            // Gắn event listeners cho nút điều hướng
            const prevBtn = document.querySelector('.carousel-nav.prev');
            const nextBtn = document.querySelector('.carousel-nav.next');
            
            console.log('Prev button found:', !!prevBtn);
            console.log('Next button found:', !!nextBtn);
            
            if (prevBtn) {
                prevBtn.addEventListener('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    console.log('Prev button clicked');
                    window.changeSlide(-1);
                });
            }
            
            if (nextBtn) {
                nextBtn.addEventListener('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    console.log('Next button clicked');
                    window.changeSlide(1);
                });
            }
            
            // Initialize carousel
            updateCarousel();
            
            // Bắt đầu auto-play
            startAutoPlay();
            
            // Dừng auto-play khi hover vào carousel
            const carouselContainer = document.querySelector('.carousel-container');
            if (carouselContainer) {
                carouselContainer.addEventListener('mouseenter', stopAutoPlay);
                carouselContainer.addEventListener('mouseleave', startAutoPlay);
            }
            
            console.log('Carousel initialized successfully');
        }
        
        // Khởi tạo khi DOM ready
        if (document.readyState === 'loading') {
            document.addEventListener('DOMContentLoaded', initCarousel);
        } else {
            // DOM đã sẵn sàng
            initCarousel();
        }
    </script>

    <!-- Features Section -->
    <section class="features">
        <div class="container">
            <h2>Tại Sao Chọn Chúng Tôi?</h2>
            <div class="features-grid">
                <div class="feature-card">
                    <div class="feature-icon">🚀</div>
                    <h3>Giao Hàng Nhanh</h3>
                    <p>Giao hàng trong vòng 24h tại TP.HCM</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon">💎</div>
                    <h3>Chất Lượng Cao</h3>
                    <p>Sản phẩm chính hãng, bảo hành đầy đủ</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon">💰</div>
                    <h3>Giá Cả Hợp Lý</h3>
                    <p>Giá tốt nhất thị trường với nhiều ưu đãi</p>
                </div>
                <div class="feature-card">
                    <div class="feature-icon">🛡️</div>
                    <h3>Bảo Hành Tốt</h3>
                    <p>Bảo hành chính hãng, hỗ trợ 24/7</p>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Products Section -->
    <section class="products" style="padding: 3rem 0; background:#ffffff;">
        <div class="container">
            <h2 style="text-align:center; margin-bottom:1.25rem;">Sản phẩm nổi bật</h2>
            <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap:1.5rem;">
                <c:forEach var="product" items="${products}">
                    <div style="border:1px solid #e5e5ea; border-radius:12px; padding:1rem; text-align:left; background:#fff;">
                        <a href="${pageContext.request.contextPath}/products/view?id=${product.id}" style="text-decoration:none; color:inherit;">
                            <div style="height:160px; display:flex; align-items:center; justify-content:center; overflow:hidden; margin-bottom:0.75rem;">
                                <c:choose>
                                    <c:when test="${not empty product.image}">
                                        <img src="${pageContext.request.contextPath}/${product.image}" alt="${product.productName}" style="max-width:100%; max-height:100%; object-fit:contain;">
                                    </c:when>
                                    <c:otherwise>
                                        <div style="width:100%; height:100%; display:flex; align-items:center; justify-content:center; color:#888;">📱</div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <div style="font-weight:600; margin-bottom:0.25rem;">${product.productName}</div>
                            <div style="color:#666; font-size:0.9rem; margin-bottom:0.5rem;">${product.manufacturer}</div>
                            <div style="font-weight:700; color:#0071e3; margin-bottom:0.5rem;">
                                <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true"/>₫
                            </div>
                            <div style="font-size:0.9rem; color:#888;">
                                <c:choose>
                                    <c:when test="${product.quantityInStock == 0}">Hết hàng</c:when>
                                    <c:otherwise>Tồn kho: ${product.quantityInStock}</c:otherwise>
                                </c:choose>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div style="display:flex; justify-content:center; gap:0.5rem; margin-top:1.5rem;">
                    <c:if test="${currentPage > 1}">
                        <a class="btn" href="${pageContext.request.contextPath}/?page=${currentPage - 1}">« Trước</a>
                    </c:if>
                    <c:forEach var="p" begin="1" end="${totalPages}">
                        <c:choose>
                            <c:when test="${p == currentPage}">
                                <span class="btn btn-secondary" style="background:#e5e5ea; color:#1a1a1a;">${p}</span>
                            </c:when>
                            <c:otherwise>
                                <a class="btn" href="${pageContext.request.contextPath}/?page=${p}">${p}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a class="btn" href="${pageContext.request.contextPath}/?page=${currentPage + 1}">Tiếp »</a>
                    </c:if>
                </div>
            </c:if>
        </div>
    </section>

    <!-- Footer -->
    <footer class="footer">
        <div class="container">
            <p>&copy; 2024 Mobile Store. Tất cả quyền được bảo lưu.</p>
            <p>Địa chỉ: 123 Đường ABC, Quận XYZ, TP.HCM | Hotline: 0123-456-789</p>
        </div>
    </footer>
</body>
</html>

