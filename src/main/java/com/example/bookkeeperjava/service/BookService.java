package com.example.bookkeeperjava.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bookkeeperjava.model.Book;
import com.example.bookkeeperjava.model.User;
import com.example.bookkeeperjava.repository.BookRepository;
import com.example.bookkeeperjava.repository.UserRepository;

@Service
public class BookService {
    @Autowired
    public BookRepository bookRepository;

    @Autowired
    public UserRepository userRepository;
    
    public void createNewBook(Book book, String username) {
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        book.setUser(user);
        bookRepository.save(book);
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByUsername(String username) {
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        return bookRepository.findByUser(user);
    }

    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id);
    }

    public void updateBook(Long id, Book book, String username) {
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        Book existingBook = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("書籍が見つかりません"));
        
        if (!existingBook.getUser().getUserName().equals(username)) {
            throw new RuntimeException("この書籍を更新する権限がありません");
        }

        book.setId(id);
        book.setUser(user);
        bookRepository.save(book);
    }

    public void deleteBook(Long id, String username) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("書籍が見つかりません"));
        
        if (!book.getUser().getUserName().equals(username)) {
            throw new RuntimeException("この書籍を削除する権限がありません");
        }

        bookRepository.deleteById(id);
    }
}
