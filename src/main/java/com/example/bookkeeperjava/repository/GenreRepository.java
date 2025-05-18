package com.example.bookkeeperjava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookkeeperjava.model.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Genre findByGenre(String genre);
}
