package com.mobilestore.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mobilestore.entity.User;
import com.mobilestore.entity.Role;
import com.mobilestore.dao.UserDAO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;

/**
 * Service xử lý đăng nhập bằng Google OAuth2
 */
public class GoogleOAuthService {
    
    // Google Client ID từ Google Cloud Console
    private static final String CLIENT_ID = "744565146565-ncqm19qq1eq9d0cq7l0q9k4ig88bnel5.apps.googleusercontent.com";
    
    // Google JWKS URI để lấy public keys
    private static final String GOOGLE_JWKS_URI = "https://www.googleapis.com/oauth2/v3/certs";
    
    private final UserDAO userDAO = new UserDAO();
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    
    /**
     * Verify Google ID token và lấy thông tin user
     * @param idTokenString ID token từ client
     * @return User nếu thành công, null nếu thất bại
     */
    public User verifyAndGetUser(String idTokenString) {
        try {
            // Gọi Google để lấy thông tin user từ ID token
            String userInfo = getGoogleUserInfo(idTokenString);
            
            if (userInfo != null) {
                JsonObject json = gson.fromJson(userInfo, JsonObject.class);
                
                String googleId = json.get("sub").getAsString();
                String email = json.has("email") ? json.get("email").getAsString() : null;
                String name = json.has("name") ? json.get("name").getAsString() : null;
                
                if (email == null) {
                    System.err.println("Google token không chứa email");
                    return null;
                }
                
                // Tìm user theo google oauth_id
                User user = userDAO.findByOauthId(googleId, "google");
                
                if (user == null) {
                    // Tạo user mới nếu chưa tồn tại
                    user = new User();
                    user.setUsername(name != null ? name : email.split("@")[0]);
                    user.setPassword(null); // OAuth users không cần password
                    user.setOauthProvider("google");
                    user.setOauthId(googleId);
                    user.setEmail(email);
                    
                    Role role = new Role();
                    role.setName("CUSTOMER");
                    role.setDescription("customer role");
                    user.setRole(role);
                    
                    user = userDAO.createWithOAuth(user);
                }
                
                return user;
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi verify Google token: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Gọi Google UserInfo API để xác thực token và lấy thông tin user
     */
    private String getGoogleUserInfo(String idToken) {
        try {
            // Sử dụng Google UserInfo endpoint để xác thực token
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://oauth2.googleapis.com/tokeninfo?id_token=" + idToken))
                .GET()
                .build();
            
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                System.err.println("Google API trả về lỗi: " + response.statusCode() + " - " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Lỗi khi gọi Google API: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
