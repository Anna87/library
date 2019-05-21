package com.customer.java.controllers;

import com.customer.java.Dto.BookDto;
import com.customer.java.common.JsonParserHelper;
import com.customer.java.models.Book;
import com.customer.java.services.BookService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    JsonParserHelper jsonParserHelper;

    @GetMapping("/books")
    public String books() {
        return bookService.GetAllBooks();
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(path = "/addBook")
    public Book newBook(@RequestParam("file") MultipartFile data, @RequestParam("bookProps") String bookProps) {
        return bookService.AddBook(data, bookProps);
    }

    @Secured(value = {"ROLE_USER", "ROLE_ADMIN"})
    @PostMapping(path = "/downloadBookFileData")
    public ResponseEntity<Resource> download(@RequestBody String fileId) throws IOException {
        MultipartFile file = bookService.DownloadDigitalBook(fileId);
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(new ByteArrayResource(bytes,file.getName()));
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(path = "/editBook")
    public Book editBook(@RequestBody BookDto dto) {
        return bookService.editBook(dto);
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(path = "/deleteBook")
    public boolean deleteBook(@RequestBody BookDto dto) {
        return bookService.deleteBook(dto);
    }
}
