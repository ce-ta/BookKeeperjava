package com.example.bookkeeperjava.util;

import java.security.Key;
import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    //秘密鍵を設定（本体はenvファイルで管理する）
    private final String SECRET_KEY = "mysecretkeymysecretkeymysecretkey12"; // 32文字以上必須

    private Key getSignKey() {
        //hmacShaKeyFor()で秘密鍵を作成する
        //getBytes()で文字列をbyteに変換する
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
            //トークン名を設定
            .setSubject(username)
            //トークンを発行した時間を設定
            .setIssuedAt(new Date(System.currentTimeMillis()))
            //トークンの有効期限を設定
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            //署名するためのアルゴリズムを設定
            .signWith(getSignKey(), SignatureAlgorithm.HS256)
            //JWTを文字列として生成
            .compact();
    }

    public String extractUsername(String token) {
                //トークンを解析するための署名鍵を設定
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            //トークンを解析してクレーム（主要な部分）を取得
            .parseClaimsJws(token)
            //ボディからトークン名(ユーザー名)を取得
            .getBody()
            .getSubject();
    }
    //JWTが有効かを検証する
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //トークンが期限切れかを確認する
    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(getSignKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            //JWTのクレームから期限日を取得する
            .getExpiration();
    }
}