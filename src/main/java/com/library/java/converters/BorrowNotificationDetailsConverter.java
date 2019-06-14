package com.library.java.converters;

import com.library.java.dto.responses.BorrowNotificationDetails;
import com.library.java.models.Book;
import com.library.java.models.Borrow;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BorrowNotificationDetailsConverter implements GenericConverter<BorrowNotificationDetails,Borrow> {
    @Override
    public BorrowNotificationDetails convert(final Borrow borrow) {
        return BorrowNotificationDetails.builder()
                .holderFirstName(borrow.getHolder().getFirstName())
                .holderLastName(borrow.getHolder().getLastName())
                .holderEmail(borrow.getHolder().getEmail())
                .books(borrow.getBooks().stream().map(Book::getTitle).collect(Collectors.toList()))
                .expiredDate(borrow.getExpiredDate().getTime())
                .build();
    }
}
