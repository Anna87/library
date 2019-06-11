package com.library.java.controllers;

import com.library.java.converters.BookDetailsConverter;
import com.library.java.dto.requests.BookCreationRequest;
import com.library.java.dto.requests.BookUpdateRequest;
import com.library.java.dto.responses.BookDetails;
import com.library.java.services.BookService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final BookDetailsConverter bookDetailsConverter;

    @GetMapping
    public List<BookDetails> getAll() {
        return bookDetailsConverter.convertList(bookService.getAll());
    }

    @Secured(value = {"ROLE_ADMIN"})
    @PostMapping(path = "/add")
    public BookDetails newBook(
            @RequestPart(value = "bookCreationRequest") final BookCreationRequest bookCreationRequest,
            @RequestPart(value = "file", required = false) final MultipartFile data
    ) {
        return bookDetailsConverter.convert(bookService.addBook(data, bookCreationRequest));
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
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}/delete")
    public void deleteBook(@NotBlank @PathVariable("id") final String id) {
        bookService.deleteBook(id);
    }
}
