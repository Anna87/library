package com.library.java.converters;

import com.google.common.base.Joiner;
import com.library.java.dto.responses.NotificationDetails;
import com.library.java.models.Book;
import com.library.java.models.Borrow;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BorrowNotificationDetailsConverter implements GenericConverter<NotificationDetails,Borrow> {
    @Override
    public NotificationDetails convert(final Borrow borrow) {
        final Map<String,Object> templateParam = new HashMap<>();
        templateParam.put("firstName", borrow.getHolder().getFirstName());
        templateParam.put("lastName", borrow.getHolder().getLastName());
        templateParam.put("books", Joiner.on(", ").join(borrow.getBooks().stream().map(Book::getTitle).collect(Collectors.toList())));
        templateParam.put("expired", borrow.getExpiredDate());

        return NotificationDetails.builder()
                .recipient(borrow.getHolder().getEmail())
                .subject("Borrow have already expired")
                .templateParam(templateParam)
                .templateName("borrowExpiredNotification.ftl")
                .build();
    }
}
