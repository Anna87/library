package com.library.java.converters;

import com.library.java.dto.responses.BookDetails;
import com.library.java.models.Book;
import org.springframework.stereotype.Component;

@Component
public class BookDetailsConverter implements GenericConverter<BookDetails,Book> {

    @Override
    public BookDetails convert(final Book book) {
        return BookDetails.builder().title(book.getTitle()).autor(book.getAuthor()).isAvalible(book.isAvailable())
                .hasDigitalFormat(book.isHasDigitalFormat()).fileId(book.getFileId()).fileName(book.getFileName())
                .id(book.getId().toString()).build();
    }
}
