package com.example.bookkeeperjava.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;

    // コンストラクタ
    public AuthRequest() {}

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}