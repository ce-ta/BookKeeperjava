package com.example.bookkeeperjava.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookkeeperjava.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);

    // ユーザー名が存在するか確認するメソッド
    boolean existsByUserName(String userName);
}
