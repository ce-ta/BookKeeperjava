package com.example.bookkeeperjava.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/logout")
public class LogoutController {

    @PostMapping
    //HttpServletRequestはHTTPリクエスト情報を受け取るクラス
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            if (authentication != null) {
                //SecurityContextLogoutHandlerはSpringSecurityのログアウト処理を行うクラス
                //無効にしたいものをlogout()の引数に入れる
                new SecurityContextLogoutHandler().logout(request, response, authentication);
            }
            return ResponseEntity.ok("ログアウト成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ログアウト処理に失敗しました");
        }
    }
}