package com.example.bookkeeperjava.dto;

public class AuthRequest {
    private String username;
    private String password;

    // コンストラクタ
    public AuthRequest() {}

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ゲッター・セッター
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}