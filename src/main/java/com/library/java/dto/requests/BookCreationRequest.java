package com.library.java.dto.requests;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Builder
@Value
public class BookCreationRequest {
    @NotBlank
    private final String title;
    @NotBlank
    private final String author;
    private final boolean isAvailable;
    private final boolean hasDigitalFormat;
    private final String fileName;

}
