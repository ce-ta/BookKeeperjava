// package com.example.bookkeeperjava.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.Authentication;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.ModelAttribute;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import com.example.bookkeeperjava.model.Book;
// import com.example.bookkeeperjava.service.BookService;

// @Controller
// @CrossOrigin(origins = "http://localhost:3000")
// public class BookController {
//     @Autowired
//     private BookService bookService;

//     @GetMapping("/book/new")
//     public String getNewBook(Model model) {
//         model.addAttribute("book", new Book());
//         return "book/new";
//     }

//     @PostMapping("/book/new")
//     public String postNewBook(@ModelAttribute Book book, Authentication authentication, RedirectAttributes redirectAttributes) {
//         if (book == null) {
//             throw new IllegalArgumentException();
//         }
//         bookService.createNewBook(book, authentication.getName());
//         redirectAttributes.addFlashAttribute("complate", "書籍を登録しました");
//         return "redirect:/index";
//     }

//     @GetMapping("/book/{id}/edit")
//     public String getEditBook(@PathVariable Long id, Model model) {
//         Book book = bookService.getBook(id).get();
//         model.addAttribute("book", book);
//         return "book/edit";
//     }

//     @PostMapping("/book/{id}/edit")
//     public String postEditBook(@PathVariable Long id, @ModelAttribute Book book, Authentication authentication, RedirectAttributes redirectAttributes) {
//         book.setId(id);
//         bookService.updateBook(id, book, authentication.getName());
//         redirectAttributes.addFlashAttribute("complate", "書籍情報を更新しました");
//         return "redirect:/index";
//     }

//     @PostMapping("/book/{id}/delete")
//     public String deleteBook(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
//         bookService.deleteBook(id, authentication.getName());
//         redirectAttributes.addFlashAttribute("complate", "書籍を削除しました");
//         return "redirect:/index";
//     }
// }
