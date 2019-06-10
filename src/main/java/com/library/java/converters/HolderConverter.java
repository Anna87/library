package com.library.java.converters;

import com.library.java.dto.requests.HolderCreationRequest;
import com.library.java.models.Holder;
import org.springframework.stereotype.Component;

@Component
public class HolderConverter implements GenericConverter<Holder,HolderCreationRequest> {

    @Override
    public Holder convert(final HolderCreationRequest request) {
        return Holder.builder().
                firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail()).build();
    }
}
