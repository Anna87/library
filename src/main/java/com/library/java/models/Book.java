package com.library.java.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder(toBuilder = true)
@Value
public class Book {
    @Id
    private final String id;
    private final String title;
    private final String autor;
    private final Boolean isAvalible;
    private final Boolean hasDigitalFormat;
    private final String fileId;
    private final String fileName;
}
