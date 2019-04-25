package com.customer.java.Dto;

import lombok.*;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolderDto {
    String id;
    String firstName;
    String lastName;
    String email;
}
