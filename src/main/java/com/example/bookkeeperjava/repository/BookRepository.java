package com.example.bookkeeperjava.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.bookkeeperjava.model.Book;
import com.example.bookkeeperjava.model.User;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByUser(User user);

    //@Queryは指定したSQLを実行するメソッドを作成できる
    //実行するSQL文を指定。(タイトルまたは作者名で検索）
    //PageableにBookServiceでSortメソッドで情報が入っているので、ソートが実行できる
    @Query("SELECT b FROM Book b WHERE b.user = :user AND (LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<Book> searchBooks(@Param("query") String query, @Param("user") User user, Pageable pageable);


    @Query("SELECT b FROM Book b")
    List<Book> findAllSorted(Pageable pageable);
}
