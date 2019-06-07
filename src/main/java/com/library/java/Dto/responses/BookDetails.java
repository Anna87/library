package com.library.java.Dto.responses;


import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class BookDetails {
    private final String id;
    private final String title;
    private final String autor;
    private final Boolean isAvalible;
    private final Boolean hasDigitalFormat;
    private final String fileId;
    private final String fileName;
}
