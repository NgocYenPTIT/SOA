package com.example.authservice.service;

import com.example.authservice.DTOs.LoginResponse;
import com.example.authservice.DTOs.LoginRequest;
import com.example.authservice.DTOs.UserDto;
import com.example.authservice.security.JwtTokenProvider;
import com.example.authservice.util.ServiceAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final RestTemplate restTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    private final ServiceAPI serviceAPI;


    @Value("${app.global.url.user-service}")
    private String userServiceUrl;

    @Autowired
    public AuthService(RestTemplate restTemplate, JwtTokenProvider jwtTokenProvider, ServiceAPI serviceAPI) {
        this.restTemplate = restTemplate;
        this.jwtTokenProvider = jwtTokenProvider;
        this.serviceAPI = serviceAPI;

    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        // Gọi đến microservice ở cổng 8889 để lấy thông tin user
        String userServiceUrl = this.userServiceUrl + "user?username=" + loginRequest.getUsername();

        try {
            // Gọi API và nhận kết quả
            ResponseEntity<UserDto> response = restTemplate.getForEntity(userServiceUrl, UserDto.class);
            UserDto user = response.getBody();

            if (user == null) {
                return ResponseEntity.status(401).body("Sai username hoặc password");
            }

            // Kiểm tra password
            if (user.getPassword().equals(loginRequest.getPassword())) {
                String token = jwtTokenProvider.createToken(user.getId(), user.getUsername());
                return ResponseEntity.ok(new LoginResponse(token, user));
            } else {
                return ResponseEntity.status(401).body("Sai username hoặc password");
            }
        } catch (Exception e) {
            // Xử lý khi không thể kết nối đến service hoặc user không tồn tại
            return ResponseEntity.status(401).body("Không thể xác thực: " + e.getMessage());
        }
    }

    public ResponseEntity<?> validateActiveUser(Long id,String token) {
        // Gọi đến microservice ở cổng 8889 để lấy thông tin user
        String userServiceUrl = this.userServiceUrl + "user/" + id ;
        try {
            // Gọi API và nhận kết quả
            UserDto user = this.serviceAPI.call(userServiceUrl, HttpMethod.GET, null, UserDto.class,token);
            if (user == null) {
                return ResponseEntity.status(401).body("Không thể xác thực: ");
            }
            return ResponseEntity.ok(true) ;
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(401).body("Không thể xác thực: " + e.getMessage());
        }
    }
}