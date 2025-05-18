package com.example.bookkeeperjava.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookkeeperjava.dto.AuthRequest;
import com.example.bookkeeperjava.dto.AuthResponse;
import com.example.bookkeeperjava.service.CustomUserDetailsService;
import com.example.bookkeeperjava.service.UserService;
import com.example.bookkeeperjava.util.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    //<?>はどんな型でも良いとういう意味（ワイルドカード）
    //@RequestBodyアノテーションはHTTPリクエストのボディ部分を受け取るために使う
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest request) {
        //リクエストボディのNameを取り出し、同じユーザーがいるかを確認する
        if (userService.existsByUsername(request.getUsername())) {
            //ResponseEntityはHTTPレスポンス全体を表すクラス
            return ResponseEntity
                //400Bad Requestを返す設定をするメソッド
                .badRequest()
                //レスポンスボディに設定する内容を設定する
                .body(Map.of("message", "このユーザー名はすでに使用されています"));
        }
        //ユーザーを作成する
        userService.saveUser(request);

        //ok()で200　OKを返す
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            //authenticationManagerはSpringSecurityで認証処理を行うクラス
            //authenticate()はユーザー認証を実行するメソッド
            authenticationManager.authenticate(
                //ユーザー名とパスワードを使って認証を行うためのクラス
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        //失敗したときに出る例外
        } catch (BadCredentialsException e) {
            throw new Exception("ユーザー名かパスワードが違います");
        }

        //ユーザー名をもとに認証情報を取得する（認証後のユーザー情報を取得するために１度userDetailsServiceでユーザー名を取得する）
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        //作成した認証情報からJWTトークンを作成する
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        //生成したトークンを200 OKで返す
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}