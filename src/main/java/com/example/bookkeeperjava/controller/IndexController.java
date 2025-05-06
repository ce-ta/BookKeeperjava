package com.example.bookkeeperjava.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.bookkeeperjava.model.Book;
import com.example.bookkeeperjava.service.BookService;

@Controller
public class IndexController {
    @Autowired
    public BookService bookservice;

    @GetMapping("/index")
    public String getIndex(Model model){
        List<Book> books = bookservice.getBooks();
        model.addAttribute("books", books);
        return "index";
    }
}
