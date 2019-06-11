package com.library.java.dto.requests;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder(toBuilder = true)
@Value
public class BookCreationRequest {
    @Deprecated
    private final String id;
    @NotBlank
    private final String title;
    @NotBlank
    private final String author;
    private final boolean isAvalible;
    private final boolean hasDigitalFormat;
    @Deprecated
    private final String fileId;
    private final String fileName;
}
