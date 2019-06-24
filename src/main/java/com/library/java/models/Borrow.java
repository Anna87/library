package com.library.java.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Document
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Borrow {
    @Id
    private String id;
    private Holder holder;
    private List<Book> books; // TODO ArrayList
    private Date expiredDate;
}
