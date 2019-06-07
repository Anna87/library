package com.library.java.models;


import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Document
@Builder(toBuilder = true)
@Value
public class Holder {
    @Id
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;

    @PersistenceConstructor
    public Holder(String id, String firstName, String lastName, String email)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
