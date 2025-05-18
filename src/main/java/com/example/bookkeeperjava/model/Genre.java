package com.example.bookkeeperjava.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Genre {
    @Id
    private Long id;
    private String genre;
}
