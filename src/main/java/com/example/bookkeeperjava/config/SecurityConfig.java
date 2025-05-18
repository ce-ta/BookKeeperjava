package com.example.bookkeeperjava.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.example.bookkeeperjava.filter.JwtFilter;
import com.example.bookkeeperjava.service.CustomUserDetailsService;

@Configuration
//Spring securityを有効にするアノテーション
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtFilter jwtFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            //CORSの設定を有効にする
            .cors(Customizer.withDefaults())
            //CSRF保護を無効にする（基本的に無効にする）
            .csrf(AbstractHttpConfigurer::disable)
            //HTTPリクエストの認証ルールを設定する
            .authorizeHttpRequests(auth -> auth
                //permitAll()で認証なしでもアクセス可
                .requestMatchers("/auth/**").permitAll()
                //残り全てのリソースへのアクセスは認証がないとアクセスできなくする
                .anyRequest().authenticated()
            )
            //sessionManagement()セッション管理に関する設定を行う
            //ステートレスなセッションにするように設定
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        //JWYフィルターを先に入れる設定。これにより、ログイン時もフィルターをかけれる
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    
        return http.build();
    }    

    //AuthenticationManagerはユーザー認証を管理するコンポーネント(SpringSecurityが使う)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    //パスワードを暗号化するエンコーダー
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //DaoAuthenticationProviderはユーザー情報をDBから取得するプロバイダー
    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //UserDetailsServiceを設定し、DBからユーザー情報を取得する
        authProvider.setUserDetailsService(userDetailsService);
        //パスワード認証用のエンコーダーを設定
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // CORS設定を手動で定義
    @Bean
    public CorsFilter corsFilter() {
        return new CorsFilter(corsConfigurationSource());  // CorsConfigurationSourceを使用
    }

    // CorsConfigurationSourceを設定
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);  // 認証情報の共有を許可
        config.addAllowedOrigin("http://localhost:3000");  // フロントエンドのURLを許可
        config.addAllowedHeader("*");  // 許可するヘッダーの種類を設定
        config.addAllowedMethod("*"); //許可するメソッドの種類を設定

        //URLごとにCORS設定を行適用するためのクラス
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        //全てのURLに対して作成したconfigポリシーを使うように設定
        source.registerCorsConfiguration("/**", config); 

        // CorsConfigurationSourceを返す
        return source;
    }

    // UserDetailsServiceを設定
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();  // CustomUserDetailsServiceの実装を指定
    }
}