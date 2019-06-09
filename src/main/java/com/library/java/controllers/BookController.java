package com.library.java.controllers;

import com.library.java.Dto.requests.BookUpdateRequest;
import com.library.java.Dto.responses.BookDetails;
import com.library.java.converters.BookDetailsConverter;
import com.library.java.services.BookService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final BookDetailsConverter bookDetailsConverter; //TODO add notNull converter, check converter pattern.

    @GetMapping
    public String getAll() {
        return bookService.getAll();
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(path = "/add")
    public BookDetails newBook(
            @RequestParam(value = "file", required = false) final MultipartFile data,
            @RequestParam("bookProps") final String bookProps
    ) {
        return bookDetailsConverter.convert(bookService.addBook(data, bookProps));
    }

    @Secured(value = {"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@NotBlank @PathVariable("id") final String id) throws IOException {
        final MultipartFile file = bookService.downloadDigitalBook(id);
        final byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(new ByteArrayResource(bytes, file.getName()));
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PatchMapping("/{id}/edit")
    public BookDetails editBook(
            @NotBlank @PathVariable("id") final String id,
            @Valid @RequestBody final BookUpdateRequest bookUpdateRequest
    ) {
        return bookDetailsConverter.convert(bookService.editBook(id, bookUpdateRequest));
    }

    @Secured(value = {"ROLE_ADMIN"})
    @GetMapping("/{id}/delete")
    public boolean deleteBook(@NotBlank @PathVariable("id") final String id) {
        return bookService.deleteBook(id);
    }
}
