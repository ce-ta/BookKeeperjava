package com.example.bookkeeperjava.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "user_table")
public class User {
    @Id
    @GeneratedValue
    private Long userId;
    private String userName;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Book> books;
}
