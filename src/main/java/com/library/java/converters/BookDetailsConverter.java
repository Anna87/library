package com.library.java.converters;

import com.library.java.Dto.responses.BookDetails;
import com.library.java.models.Book;
import org.springframework.stereotype.Component;

@Component
public class BookDetailsConverter implements GenericConverter<BookDetails,Book> {

    @Override
    public BookDetails convert(final Book book) {
        return BookDetails.builder().title(book.getTitle()).autor(book.getAutor()).isAvalible(book.getIsAvalible())
                .hasDigitalFormat(book.getHasDigitalFormat()).fileId(book.getFileId()).fileName(book.getFileName())
                .id(book.getId()).build();
    }
}
