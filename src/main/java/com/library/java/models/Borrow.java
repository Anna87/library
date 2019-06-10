package com.library.java.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Document
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Borrow {
    @Id
    private String id;//TODO replace with UUID
    private Holder holder;
    private List<Book> books;
    private Date expiredDate;

}
