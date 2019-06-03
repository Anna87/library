package com.library.java.Dto;

import lombok.*;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    String id;
    String title;
    String autor;
    Boolean isAvalible;
    Boolean hasDigitalFormat;
    String fileId;
    String fileName;
}
