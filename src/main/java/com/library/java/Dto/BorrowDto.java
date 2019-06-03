package com.library.java.Dto;

import com.library.java.models.Book;
import com.library.java.models.Holder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowDto {
    Holder holder;
    Book[] books;
    long expiredDate;
}
