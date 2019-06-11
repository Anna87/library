package com.library.java.converters;

import com.library.java.dto.requests.BookCreationRequest;
import com.library.java.models.Book;
import org.springframework.stereotype.Component;

@Component
public class BookConverter implements GenericConverter<Book, BookCreationRequest> {

    @Override
    public Book convert(final BookCreationRequest request) {
        return Book.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .isAvailable(request.isAvalible())
                .build();
    }
}
