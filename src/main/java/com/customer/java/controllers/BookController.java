package com.customer.java.controllers;

import com.customer.java.Dto.BookDto;
import com.customer.java.common.JsonParserHelper;
import com.customer.java.models.Book;
import com.customer.java.services.BookService;
import com.customer.java.services.StorageClient;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BookController {

    @Autowired
    BookService bookService;
/*
    @Autowired
    private StorageClient storageClient;
*/


    @Autowired
    JsonParserHelper jsonParserHelper;

    @GetMapping("/books")
    public String books() {
        return bookService.GetAllBooks();
    }

    /*@PostMapping(path = "/addBook")
    public Book newBook(@RequestBody BookDto dto) {
        return bookService.AddBook(dto);
    }*/

    @PostMapping(path = "/addBook")
    public Book newBook(@RequestParam("file") MultipartFile data, @RequestParam("bookProps") String bookProps) {
        BookDto bookDto1 = jsonParserHelper.ReadValue(bookProps, BookDto.class);

        StorageClient storageClient = Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(StorageClient.class, "http://localhost:8000");

        storageClient.AddDigitalBook(bookDto1.getTitle());
//        String users = storageClient.AddDigitalBook("xxx");

        return bookService.AddBook(bookDto1);
        //return Book.builder().autor("ff").id("1").isAvalible(true).title("g").build();
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
