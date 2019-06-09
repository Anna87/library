package com.library.java.converters;

import com.google.common.collect.Lists;
import com.library.java.Dto.BorrowDto;
import com.library.java.models.Borrow;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class BorrowConverter implements GenericConverter<Borrow,BorrowDto> {

    @Override
    public Borrow convert(final BorrowDto dto) {
        return Borrow.builder()
                .books(Lists.newArrayList(dto.getBooks()))
                .holder(dto.getHolder())
                .expiredDate(new Date(dto.getExpiredDate()))
                .build();
    }
}
