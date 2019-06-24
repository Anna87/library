package com.library.java.services;

import com.google.common.collect.Lists;
import com.library.java.client.StorageClient;
import com.library.java.converters.BookConverter;
import com.library.java.dto.requests.BookCreationRequest;
import com.library.java.dto.requests.BookUpdateRequest;
import com.library.java.exceptions.NotFoundException;
import com.library.java.models.Book;
import com.library.java.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final String bookNotFound = "Book not found";

    private final BookRepository bookRepository;

    private final BorrowService borrowService;

    private final StorageClient storageClient;

    private final BookConverter bookConverter;

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book addBook(MultipartFile data, BookCreationRequest bookCreationRequest) {
        final Book newBook = bookConverter.convert(bookCreationRequest);

        if (data != null && bookCreationRequest.isHasDigitalFormat()) {
            final String fileId = storageClient.addDigitalBook(data, bookCreationRequest.getTitle(), bookCreationRequest.getAuthor());
            newBook.setFileId(fileId);
            newBook.setFileName(bookCreationRequest.getFileName());
            newBook.setHasDigitalFormat(true);
            return bookRepository.save(newBook);
        }

        return bookRepository.save(newBook);
    }

    public MultipartFile downloadDigitalBook(String fileId) throws IOException {
        return storageClient.downloadDigitalBook(fileId);
    }

    @Transactional//TODO add test
    public Book editBook(final String  id, final BookUpdateRequest bookUpdateRequest) {
        final Book bookForUpdate = findById(id);

        final Book updatedBook = bookForUpdate.toBuilder()
                .author(bookUpdateRequest.getAuthor())
                .isAvailable(bookUpdateRequest.isAvailable())
                .title(bookUpdateRequest.getTitle())
                .build();

        final Book savedBook = bookRepository.save(updatedBook);

        borrowService.updateBookInBorrow(updatedBook);

        return savedBook;
    }

    @Transactional
    public void deleteBook(final String id) {
        final Book book = findById(id);
        bookRepository.delete(book);
        borrowService.removeBookFromBorrows(id);
    }

    public Book findById(final String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(bookNotFound));
    }

    public void setBooksUnavailable(final List<Book> books) {
        for (Book item : books) {
            bookRepository.save(item.toBuilder().isAvailable(false).build());
        }
    }

    public List<Book> findAllById(List<String> bookIds){
        return Lists.newArrayList(bookRepository.findAllById(bookIds));
    }

}
