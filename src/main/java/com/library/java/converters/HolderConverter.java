package com.library.java.converters;

import com.library.java.Dto.HolderDto;
import com.library.java.models.Holder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class HolderConverter implements GenericConverter<Holder,HolderDto> {

    @Override
    public Holder convert(final HolderDto dto) {
        Holder holder = Holder.builder().firstName(dto.getFirstName()).lastName(dto.getLastName()).email(dto.getEmail()).build();
        if(!Objects.equals(dto.getId(),"")) {
            return holder.toBuilder().id(dto.getId()).build();
        }
        return holder;
    }
}
