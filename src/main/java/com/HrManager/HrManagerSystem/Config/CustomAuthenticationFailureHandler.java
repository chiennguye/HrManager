package com.HrManager.HrManagerSystem.Config;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        String errorMessage = "Tài khoản hoặc mật khẩu không chính xác!!"; // Mặc định cho lỗi đăng nhập chung

        try {
            // Mã hóa thông báo lỗi để tránh ký tự không hợp lệ
            String encodedMessage = URLEncoder.encode(errorMessage, "UTF-8");
            String redirectUrl = "/login?error=true&message=" + encodedMessage;

            // Chuyển hướng đến trang đăng nhập với thông báo lỗi
            response.sendRedirect(redirectUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
