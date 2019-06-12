package com.library.java.dto.responses;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder(toBuilder = true)
@Value
public class BorrowNotificationDetails {
    String holderFullName;
    String holderEmail;
    List<String> books;
    long expiredDate;
}
