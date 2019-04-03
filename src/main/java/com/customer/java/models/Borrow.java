package com.customer.java.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.List;

@Document
@Builder(toBuilder = true)
@Setter
@Getter
public class Borrow {
    @Id
    private String id;

//    @OneToOne()
//    @JoinColumn(name="holder_id")
    public Holder holder;

//    @OneToMany(mappedBy = "borrow")
    public List<Book> books;

}
