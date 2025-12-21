package com.mobilestore.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import java.io.IOException;

/**
 * Filter để thiết lập encoding UTF-8 cho tất cả request và response
 * 
 * @author Mobile Store Team
 */
public class CharacterEncodingFilter implements Filter {
    
    private String encoding = "UTF-8";
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null && !encodingParam.isEmpty()) {
            this.encoding = encodingParam;
        }
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // Kiểm tra nếu là static resource thì bỏ qua filter
        if (request instanceof jakarta.servlet.http.HttpServletRequest) {
            jakarta.servlet.http.HttpServletRequest httpRequest = (jakarta.servlet.http.HttpServletRequest) request;
            String path = httpRequest.getRequestURI();
            
            // Bỏ qua static resources (images, css, js, fonts, etc.)
            if (path.startsWith("/images/") || 
                path.startsWith("/css/") || 
                path.startsWith("/js/") ||
                path.startsWith("/assets/") ||
                path.startsWith("/fonts/") ||
                path.contains(".") && !path.endsWith(".jsp") && !path.endsWith(".html")) {
                chain.doFilter(request, response);
                return;
            }
        }
        
        // Thiết lập encoding cho request
        request.setCharacterEncoding(encoding);
        
        // Thiết lập encoding cho response
        response.setCharacterEncoding(encoding);
        
        // Tiếp tục chuỗi filter
        chain.doFilter(request, response);
    }
    
    @Override
    public void destroy() {
        // Cleanup nếu cần
    }
}

