package com.customer.java.services;

import com.customer.java.Dto.BookDto;
import com.customer.java.client.StorageClient;
import com.customer.java.common.JsonParserHelper;
import com.customer.java.models.Book;
import com.customer.java.models.FileData;
import com.customer.java.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.IOUtils;

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

    public String GetAllBooks(){
        return jsonParserHelper.WriteToStrJson(bookRepository.findAll());
    }

    public Book AddBook(MultipartFile data, String bookProps) {
        BookDto bookDto = jsonParserHelper.ReadValue(bookProps, BookDto.class);
        String fileId =  storageClient.addDigitalBook(data,bookDto.getTitle(), bookDto.getAutor());
        bookDto.setFileId(fileId);
        return bookRepository.save(this.ConvertFromDto(bookDto));
    }

    public MultipartFile DownloadDigitalBook(String fileId) throws IOException {
        return storageClient.downloadDigitalBook(fileId);
    }

    public Book editBook(BookDto dto) {
        Book book = this.ConvertFromDto(dto);
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
        Book book = this.ConvertFromDto(dto);
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

    private Book ConvertFromDto(BookDto dto) {
        Book book = Book.builder().title(dto.getTitle()).autor(dto.getAutor()).isAvalible(dto.getIsAvalible())
                .hasDigitalFormat(dto.getHasDigitalFormat()).fileId(dto.getFileId()).fileName(dto.getFileName())
                .build();
        if(!Objects.equals(dto.getId(),"")) {
            return book.toBuilder().id(dto.getId()).build();
        }
        return book;
    }
}
