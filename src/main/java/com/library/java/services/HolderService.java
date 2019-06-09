package com.library.java.services;

import com.library.java.Dto.HolderDto;
import com.library.java.Dto.requests.HolderUpdateRequest;
import com.library.java.common.JsonParserHelper;
import com.library.java.converters.HolderConverter;
import com.library.java.exceptions.NotFoundException;
import com.library.java.models.Holder;
import com.library.java.repositories.HolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HolderService {
    private final String HOLDERNOTFOUND = "Holder not found";

    private final HolderRepository holderRepository;

    private final BorrowService borrowService;

    private final JsonParserHelper jsonParserHelper;

    private final HolderConverter holderConverter;

    public String getAllHolders() {
        return jsonParserHelper.writeToStrJson(holderRepository.findAll());
    }

    public Holder addHolder(final HolderDto dto) {
        return holderRepository.save(holderConverter.convert(dto));
    }


    public Holder editHolder(final String id, final HolderUpdateRequest holderUpdateRequest) {
        Optional<Holder> optionalHolder = holderRepository.findById(id);
        Holder holderForUpdate = optionalHolder.orElseThrow(() -> new NotFoundException(this.HOLDERNOTFOUND));
        Holder updatedHolder = holderForUpdate.toBuilder().firstName(holderUpdateRequest.getFirstName()).lastName(holderUpdateRequest.getLastName())
                .email(holderUpdateRequest.getEmail()).build();
        Holder savedHolder =  holderRepository.save(updatedHolder);
        borrowService.updateHolderInBorrow(updatedHolder);
        return savedHolder;
    }

    public boolean deleteHolder(final String id) {
        Optional<Holder> optionalHolder = holderRepository.findById(id);
        Holder holderForDelete = optionalHolder.orElseThrow(() -> new NotFoundException(this.HOLDERNOTFOUND));
        try {
            holderRepository.delete(holderForDelete);
            borrowService.deleteHolderInBorrow(holderForDelete);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
