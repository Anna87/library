package com.library.java.converters;

import com.library.java.dto.responses.HolderDetails;
import com.library.java.models.Holder;
import org.springframework.stereotype.Component;

@Component
public class HolderDetailsConverter implements GenericConverter<HolderDetails,Holder> {

    @Override
    public HolderDetails convert(final Holder holder) {
        return HolderDetails.builder()
                .firstName(holder.getFirstName())
                .lastName(holder.getLastName())
                .email(holder.getEmail())
                .id(holder.getId())
                .build();
    }
}
