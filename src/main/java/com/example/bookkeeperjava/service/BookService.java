package com.example.bookkeeperjava.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.bookkeeperjava.dto.BookRequest;
import com.example.bookkeeperjava.model.Book;
import com.example.bookkeeperjava.model.Genre;
import com.example.bookkeeperjava.model.User;
import com.example.bookkeeperjava.repository.BookRepository;
import com.example.bookkeeperjava.repository.GenreRepository;
import com.example.bookkeeperjava.repository.UserRepository;

@Service
public class BookService {
    @Autowired
    public BookRepository bookRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private GenreRepository genreRepository;
    
    // public void createNewBook(Book book, String username) {
    //     User user = userRepository.findByUserName(username)
    //         .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
    //     book.setUser(user);
    //     bookRepository.save(book);
    // }
    public void createNewBook(BookRequest bookRequest, String username) {
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
    
        Genre genre = genreRepository.findById(bookRequest.getGenreId())
            .orElseThrow(() -> new RuntimeException("ジャンルが見つかりません"));
    
        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setGenre(genre);
        book.setPublishedDate(bookRequest.getPublishedDate());
        book.setUser(user);
    
        bookRepository.save(book);
    }
    

    public Map<String, Object> getBooks(String username, int page, int size, String sortBy, String order, String query) {
        Sort sort = order.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
    
        Page<Book> bookPage;
        User user = userRepository.findByUserName(username)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
        bookPage = bookRepository.searchBooks(query, user, pageable);
    
        Map<String, Object> response = new HashMap<>();
        response.put("content", bookPage.getContent());
        response.put("page", bookPage.getNumber());
        response.put("totalPages", bookPage.getTotalPages());
    
        return response;
    }    

    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id);
    }

    public void updateBook(Long id, BookRequest bookRequest, String username) {
        userRepository.findByUserName(username)
            .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));
    
        Book existingBook = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("書籍が見つかりません"));

        Genre genre = genreRepository.findById(bookRequest.getGenreId())
            .orElseThrow(() -> new RuntimeException("ジャンルが見つかりません"));
    
        // 自分の書籍でなければエラー
        if (!existingBook.getUser().getUserName().equals(username)) {
            throw new RuntimeException("この書籍を更新する権限がありません");
        }
    
        // BookRequest のデータで既存の Book を更新
        existingBook.setTitle(bookRequest.getTitle());
        existingBook.setAuthor(bookRequest.getAuthor());
        existingBook.setPublishedDate(bookRequest.getPublishedDate());
        existingBook.setGenre(genre);

        bookRepository.save(existingBook);
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
