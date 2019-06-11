package com.library.java.dto.requests;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder(toBuilder = true)
@Value
public class BorrowCreationRequest {
    @NotBlank
    private final String holderId;
    @NotEmpty
    private final List<String> bookIds;
    @NotNull
    private final long expiredDate;
}
