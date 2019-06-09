package com.library.java.converters;

import com.library.java.Dto.BookDto;
import com.library.java.models.Book;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class BookConverter implements GenericConverter<Book,BookDto> {

    @Override
    public Book convert(final BookDto dto) {
        Book book = Book.builder().title(dto.getTitle()).autor(dto.getAutor()).isAvalible(dto.isAvalible())
                .hasDigitalFormat(dto.isHasDigitalFormat()).fileId(dto.getFileId()).fileName(dto.getFileName())
                .build();
        if (!Objects.equals(dto.getId(), "")) {
            return book.toBuilder().id(dto.getId()).build();
        }
        return book;
    }
}
