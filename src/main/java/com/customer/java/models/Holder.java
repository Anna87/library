package com.customer.java.models;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Document
@Builder(toBuilder = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Holder {
    @Id
    private String id;

    private String firstName;
    private String lastName;
}
