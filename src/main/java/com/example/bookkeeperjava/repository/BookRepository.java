package com.example.bookkeeperjava.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.bookkeeperjava.model.Book;
import com.example.bookkeeperjava.model.User;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUser(User user);
}
