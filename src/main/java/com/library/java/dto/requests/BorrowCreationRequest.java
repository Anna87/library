package com.library.java.dto.requests;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder(toBuilder = true)
@Value
public class BorrowCreationRequest {
    private final String holderId;//TODO validation
    private final List<String> bookIds;
    // TODO wtah validation
    private final long expiredDate;
}
