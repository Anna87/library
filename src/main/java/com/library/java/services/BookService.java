package com.library.java.services;

import com.library.java.Dto.BookDto;
import com.library.java.Dto.requests.BookUpdateRequest;
import com.library.java.client.StorageClient;
import com.library.java.common.JsonParserHelper;
import com.library.java.converters.BookConverter;
import com.library.java.exceptions.NotFoundException;
import com.library.java.models.Book;
import com.library.java.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final String BOOKNOTFOUND = "Book not found";

    private final BookRepository bookRepository;

    private final BorrowService borrowService;

    private final JsonParserHelper jsonParserHelper;

    private final StorageClient storageClient;

    private final BookConverter bookConverter;

    public String getAll() {
        return jsonParserHelper.writeToStrJson(bookRepository.findAll());
    }

    public Book addBook(MultipartFile data, String bookProps) {
        final BookDto bookDto = jsonParserHelper.readValue(bookProps, BookDto.class);
        if (data != null) {
            String fileId = storageClient.addDigitalBook(data, bookDto.getTitle(), bookDto.getAutor());
            return bookRepository.save(bookConverter.convert(bookDto.toBuilder().fileId(fileId).build()));
        }
        return bookRepository.save(bookConverter.convert(bookDto));
    }

    public MultipartFile downloadDigitalBook(String fileId) throws IOException {
        return storageClient.downloadDigitalBook(fileId);
    }

    public Book editBook(final String id, final BookUpdateRequest bookUpdateRequest) {
        final Book bookForUpdate =
                bookRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(this.BOOKNOTFOUND));

        final Book updatedBook = bookForUpdate.toBuilder()
                .autor(bookUpdateRequest.getAutor())
                .isAvalible(bookUpdateRequest.isAvalible())
                .title(bookUpdateRequest.getTitle())
                .build();

        final Book savedBook = bookRepository.save(updatedBook);

        borrowService.updateBookInBorrow(updatedBook);  /// TODO what???

        return savedBook;
    }

    public boolean deleteBook(String id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book bookForDelete = optionalBook.orElseThrow(() -> new NotFoundException(this.BOOKNOTFOUND));
        try {
            bookRepository.delete(bookForDelete);
            borrowService.deleteBookInBorrow(bookForDelete);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
