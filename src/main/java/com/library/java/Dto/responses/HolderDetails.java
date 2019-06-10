package com.library.java.dto.responses;


import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class HolderDetails {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String email;
}
