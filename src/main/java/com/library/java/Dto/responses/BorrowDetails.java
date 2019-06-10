package com.library.java.dto.responses;

import com.library.java.models.Book;
import com.library.java.models.Holder;
import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.List;


@Builder(toBuilder = true)
@Value
public class BorrowDetails {
    private final String id;
    private final Holder holder;
    private final List<Book> books;
    private final Date expiredDate;

}
