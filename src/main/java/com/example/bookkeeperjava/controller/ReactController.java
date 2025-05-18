package com.example.bookkeeperjava.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.bookkeeperjava.dto.BookRequest;
import com.example.bookkeeperjava.model.Book;
import com.example.bookkeeperjava.service.BookService;

import jakarta.validation.Valid;

//REST APIであることを宣言し、自動でHTTPリクエストを受け取るコントローラーとして認識し、JSON応答を返す
@RestController
@RequestMapping("/api/books")
//指定したドメインからのクロスオリジンアクセスを許可する
@CrossOrigin(origins = "http://localhost:3000")
public class ReactController {
    @Autowired
    private BookService bookService;
    
    //Authenticationは認証情報を持つクラス
    @GetMapping("/search")
    public ResponseEntity<?> getBooksByAuthenticatedUser(
        Authentication authentication,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "publishedDate") String sortBy,
        @RequestParam(defaultValue = "desc") String order,
        @RequestParam(defaultValue = "", required = false) String query
    ) {
        try {
            String username = authentication.getName();
            Map<String, Object> books = bookService.getBooks(username, page, size, sortBy, order, query);
            return ResponseEntity.ok(books);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("書籍の取得に失敗しました: " + e.getMessage());
        }
    }   

    @PostMapping("/new")
    public ResponseEntity<?> createBook(@RequestBody @Valid BookRequest bookRequest, BindingResult result, Authentication authentication) {
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        bookService.createNewBook(bookRequest, authentication.getName());
        return ResponseEntity.ok().body(bookRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody @Valid BookRequest bookRequest, BindingResult result, Authentication authentication) {
        if(result.hasErrors()){
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            bookService.updateBook(id, bookRequest, authentication.getName());
            return ResponseEntity.ok().body(bookRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("書籍の更新に失敗しました: " + e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        try {
            Optional<Book> book = bookService.getBook(id);
                            //ResponseEntity::okは「book -> ResponseEntity.ok(book)」と同じ意味
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
