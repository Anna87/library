package com.customer.java.controllers;

import com.customer.java.Dto.BookDto;
import com.customer.java.common.JsonParserHelper;
import com.customer.java.models.Book;
import com.customer.java.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    JsonParserHelper jsonParserHelper;

    @GetMapping("/books")
    public String books() {
        return bookService.GetAllBooks();
    }

    @PostMapping(path = "/addBook")
    public Book newBook(@RequestParam("file") MultipartFile data, @RequestParam("bookProps") String bookProps) {
        return bookService.AddBook(data, bookProps);
    }

    @PostMapping(path = "/downloadBook")
    public void newBook(@RequestBody String fileId) {
        bookService.DownloadDigitalBook(fileId);
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
