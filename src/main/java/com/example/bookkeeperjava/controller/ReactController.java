package com.example.bookkeeperjava.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookkeeperjava.model.Book;
import com.example.bookkeeperjava.service.BookService;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "http://localhost:3000")
public class ReactController {
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public ResponseEntity<List<Book>> getBooks(Authentication authentication) {
        try {
            List<Book> books = bookService.getBooksByUsername(authentication.getName());
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createBook(@RequestBody Book book, Authentication authentication) {
        try {
            bookService.createNewBook(book, authentication.getName());
            return ResponseEntity.ok().body(book);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("書籍の登録に失敗しました: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book book, Authentication authentication) {
        try {
            bookService.updateBook(id, book, authentication.getName());
            return ResponseEntity.ok().body(book);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("書籍の更新に失敗しました: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        try {
            Optional<Book> book = bookService.getBook(id);
            return book.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("書籍の取得に失敗しました: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id, Authentication authentication) {
        try {
            bookService.deleteBook(id, authentication.getName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("書籍の削除に失敗しました: " + e.getMessage());
        }
    }
}
