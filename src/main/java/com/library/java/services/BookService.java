package com.library.java.services;

import com.library.java.Dto.BookDto;
import com.library.java.client.StorageClient;
import com.library.java.common.JsonParserHelper;
import com.library.java.models.Book;
import com.library.java.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookService {
    private final String BOOKNOTFOUND = "Book not found";

    @Autowired
    BookRepository bookRepository;

    @Autowired
    BorrowService borrowService;

    @Autowired
    JsonParserHelper jsonParserHelper;

    @Autowired
    StorageClient storageClient;

    public String getAllBooks(){
        return jsonParserHelper.writeToStrJson(bookRepository.findAll());
    }

    public Book AddBook(MultipartFile data, String bookProps) {
        BookDto bookDto = jsonParserHelper.readValue(bookProps, BookDto.class);
        if(data != null){
            String fileId =  storageClient.addDigitalBook(data,bookDto.getTitle(), bookDto.getAutor());
            bookDto.setFileId(fileId);
        }
        return bookRepository.save(this.convertFromDto(bookDto));
    }

    public MultipartFile downloadDigitalBook(String fileId) throws IOException {
        return storageClient.downloadDigitalBook(fileId);
    }

    public Book editBook(BookDto dto) {
        Book book = this.convertFromDto(dto);
        Optional<Book> optionalBook = bookRepository.findById(book.getId());
        Book bookForUpdate = optionalBook.orElseThrow(() -> new NullPointerException(this.BOOKNOTFOUND));
        bookForUpdate.setAutor(book.getAutor());
        bookForUpdate.setIsAvalible(book.getIsAvalible());
        bookForUpdate.setTitle(book.getTitle());
        Book savedBook =  bookRepository.save(bookForUpdate);
        borrowService.updateBookInBorrow(bookForUpdate);
        return savedBook;
    }

    public boolean deleteBook(BookDto dto) {
        Book book = this.convertFromDto(dto);
        Optional<Book> optionalBook = bookRepository.findById(book.getId());
        Book bookForDelete = optionalBook.orElseThrow(() -> new NullPointerException(this.BOOKNOTFOUND));
        try {
            bookRepository.delete(bookForDelete);
            borrowService.deleteBookInBorrow(bookForDelete);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    private Book convertFromDto(BookDto dto) {
        Book book = Book.builder().title(dto.getTitle()).autor(dto.getAutor()).isAvalible(dto.getIsAvalible())
                .hasDigitalFormat(dto.getHasDigitalFormat()).fileId(dto.getFileId()).fileName(dto.getFileName())
                .build();
        if(!Objects.equals(dto.getId(),"")) {
            return book.toBuilder().id(dto.getId()).build();
        }
        return book;
    }
}
