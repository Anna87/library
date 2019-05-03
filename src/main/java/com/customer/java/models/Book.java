package com.customer.java.models;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder(toBuilder = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    private String id;
    private String title;
    private String autor;
    private Boolean isAvalible;
    private Boolean hasDigitalFormat;
    private String fileId;
    private String fileName;
}
