package com.library.java.services;

import com.library.java.Dto.BookDto;
import com.library.java.Dto.responses.BookDetails;
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

    public String getAll(){
        return jsonParserHelper.writeToStrJson(bookRepository.findAll());
    }

    public Book addBook(MultipartFile data, String bookProps) {
        final BookDto bookDto = jsonParserHelper.readValue(bookProps, BookDto.class);
        if(data != null){
            String fileId =  storageClient.addDigitalBook(data,bookDto.getTitle(), bookDto.getAutor());
            return bookRepository.save(this.convertFromDto(bookDto.toBuilder().fileId(fileId).build()));
        }
        return bookRepository.save(this.convertFromDto(bookDto));
    }

    public BookDetails convertToBookDetails(Book book){
        return BookDetails.builder().title(book.getTitle()).autor(book.getAutor()).isAvalible(book.getIsAvalible())
                .hasDigitalFormat(book.getHasDigitalFormat()).fileId(book.getFileId()).fileName(book.getFileName())
                .id(book.getId()).build();
    }

    public MultipartFile downloadDigitalBook(String fileId) throws IOException {
        return storageClient.downloadDigitalBook(fileId);
    }

    public Book editBook(String id, BookDto dto) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        Book bookForUpdate = optionalBook.orElseThrow(() -> new NullPointerException(this.BOOKNOTFOUND));
        Book updatedBook = bookForUpdate.toBuilder().autor(dto.getAutor()).isAvalible(dto.isAvalible()).title(dto.getTitle()).build();
        Book savedBook =  bookRepository.save(updatedBook);
        borrowService.updateBookInBorrow(updatedBook);
        return savedBook;
    }

    public boolean deleteBook(String id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
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
        Book book = Book.builder().title(dto.getTitle()).autor(dto.getAutor()).isAvalible(dto.isAvalible())
                .hasDigitalFormat(dto.isHasDigitalFormat()).fileId(dto.getFileId()).fileName(dto.getFileName())
                .build();
        if(!Objects.equals(dto.getId(),"")) {
            return book.toBuilder().id(dto.getId()).build();
        }
        return book;
    }
}
