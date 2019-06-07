package com.library.java.Dto;

import com.library.java.models.Book;
import com.library.java.models.Holder;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Builder(toBuilder = true)
@Value
public class BorrowDto {
    @NotNull
    private final Holder holder;
    @Min(1)
    private final Book[] books;
    // TODO wtah validation
    private final long expiredDate;
}
