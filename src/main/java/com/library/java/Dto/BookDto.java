package com.library.java.Dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder(toBuilder = true)
@Value
public class BookDto {
    private final String id;
    @NotBlank
    private final String title;
    @NotBlank
    private final String autor;
    private final boolean isAvalible;
    private final boolean hasDigitalFormat;
    private final String fileId;
    private final String fileName;
}
