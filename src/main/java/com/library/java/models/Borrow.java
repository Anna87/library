package com.library.java.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Document
@Builder(toBuilder = true)
@Setter
@Getter
public class Borrow {
    @Id
    private String id;
    public Holder holder;
    public List<Book> books;
    public Date expiredDate;

}
