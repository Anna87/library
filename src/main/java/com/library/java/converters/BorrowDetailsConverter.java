package com.library.java.converters;

import com.library.java.dto.responses.BorrowDetails;
import com.library.java.models.Borrow;
import org.springframework.stereotype.Component;

@Component
public class BorrowDetailsConverter implements GenericConverter<BorrowDetails, Borrow> {

    @Override
    public BorrowDetails convert(final Borrow borrow) {
        return BorrowDetails.builder()
                .id(borrow.getId())
                .books(borrow.getBooks())
                .holder(borrow.getHolder())
                .expiredDate(borrow.getExpiredDate())
                .build();
    }
}
