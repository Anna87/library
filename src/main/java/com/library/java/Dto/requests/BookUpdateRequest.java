package com.library.java.Dto.requests;


import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Builder(toBuilder = true)
@Value
public class BookUpdateRequest {
    @NotBlank
    private final String title;
    @NotBlank
    private final String autor;
    private final boolean isAvalible;
}
