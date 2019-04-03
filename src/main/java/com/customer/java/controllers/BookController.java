package com.customer.java.controllers;

import com.customer.java.Dto.BookDto;
import com.customer.java.models.Book;
import com.customer.java.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class BookController {

    @Autowired
    BookService bookService;

//    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/books")
    public String books() {
        return bookService.GetAllBooks();
    }

    @PostMapping(path = "/addBook")
    public Book newBook(@RequestBody BookDto dto) {
        return bookService.AddBook(dto);
    }

    @PostMapping(path = "/editBook")
    public Book editBook(@RequestBody BookDto dto) {
        return bookService.editBook(dto);
    }

    @PostMapping(path = "/deleteBook")
    public boolean deleteBook(@RequestBody BookDto dto) {
        return bookService.deleteBook(dto);
    }
}
