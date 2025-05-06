package com.example.bookkeeperjava.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.bookkeeperjava.dto.SignupRequest;
import com.example.bookkeeperjava.model.User;
import com.example.bookkeeperjava.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // ユーザーの登録
    public User createUser(String username, String password) {
        // ユーザー名の重複チェック
        if (userRepository.findByUserName(username).isPresent()) {
            throw new RuntimeException("このユーザー名は既に使用されています");
        }

        // パスワードのハッシュ化
        String encodedPassword = passwordEncoder.encode(password);

        // 新しいユーザーを作成
        User newUser = new User();
        newUser.setUserName(username);
        newUser.setPassword(encodedPassword);

        // ユーザーをデータベースに保存
        return userRepository.save(newUser);
    }

    // ユーザー名が既に存在するか確認
    public boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    // ユーザー情報を保存
    public void saveUser(SignupRequest request) {
        User user = new User();
        user.setUserName(request.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        // 必要ならRoleや他のフィールドも設定
        userRepository.save(user);
    }

    // ユーザー情報の更新
    public User updateUser(Long userId, String newUserName, String newPassword) {
        // ユーザーを取得
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        // ユーザー情報を更新
        if (newUserName != null) {
            user.setUserName(newUserName);
        }
        if (newPassword != null) {
            user.setPassword(passwordEncoder.encode(newPassword));
        }

        // 更新後のユーザーを保存
        return userRepository.save(user);
    }

    // ユーザーの削除
    public void deleteUser(Long userId) {
        // ユーザーを削除
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        userRepository.delete(user);
    }

    public Optional<User> findByUserName(String UserName){
        return userRepository.findByUserName(UserName);
    }
}
