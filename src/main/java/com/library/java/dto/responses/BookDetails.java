package com.library.java.dto.responses;


import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class BookDetails {
    private final String id;
    private final String title;
    private final String author;
    private final Boolean isAvailable;
    private final Boolean hasDigitalFormat;
    private final String fileId;
    private final String fileName;
}
