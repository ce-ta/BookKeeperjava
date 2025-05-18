package com.example.bookkeeperjava.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.bookkeeperjava.model.User;
import com.example.bookkeeperjava.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName)
            .orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません"));
        return new org.springframework.security.core.userdetails.User(
            user.getUserName(), user.getPassword(), new ArrayList<>()
        );
    }
}