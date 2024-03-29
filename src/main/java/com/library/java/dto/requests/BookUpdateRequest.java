package com.library.java.dto.requests;


import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Builder
@Value
public class BookUpdateRequest {
    @NotBlank
    private final String title;
    @NotBlank
    private final String author;
    private final boolean isAvailable;
}
