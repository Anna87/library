package com.library.java.converters;

import com.library.java.Dto.responses.BorrowDetails;
import com.library.java.models.Borrow;
import org.springframework.stereotype.Component;

@Component
public class BorrowDetailsConverter implements GenericConverter<BorrowDetails, Borrow> {

//    @Override
//    public List<BorrowDetails> convert(final List<Borrow> borrows) {
//        return borrows.stream().map(borrow ->
//                BorrowDetails.builder().id(borrow.getId()).books(borrow.getBooks()).holder(borrow.getHolder()).build())
//                .collect(Collectors.toList());
//    }

    @Override
    public BorrowDetails convert(final Borrow borrow) {
        return BorrowDetails.builder().id(borrow.getId()).books(borrow.getBooks())
                .holder(borrow.getHolder()).build();
    }
}
