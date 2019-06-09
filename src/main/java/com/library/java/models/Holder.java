package com.library.java.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Holder {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;

}
