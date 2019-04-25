package com.customer.java.Dto;

import com.customer.java.models.Book;
import com.customer.java.models.Holder;
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
