package com.library.java.services;

import com.library.java.Dto.HolderDto;
import com.library.java.Dto.responses.HolderDetails;
import com.library.java.common.JsonParserHelper;
import com.library.java.models.Holder;
import com.library.java.repositories.HolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class HolderService {
    private final String HOLDERNOTFOUND = "Holder not found";
    @Autowired
    HolderRepository holderRepository;
    @Autowired
    BorrowService borrowService;
    @Autowired
    JsonParserHelper jsonParserHelper;

    public String getAllHolders() {
        return jsonParserHelper.writeToStrJson(holderRepository.findAll());
    }

    public Holder addHolder(HolderDto dto) {
        return holderRepository.save(this.convertFromDto(dto));
    }

    private Holder convertFromDto(HolderDto dto) {
        Holder holder = Holder.builder().firstName(dto.getFirstName()).lastName(dto.getLastName()).email(dto.getEmail()).build();
        if(!Objects.equals(dto.getId(),"")) {
            return holder.toBuilder().id(dto.getId()).build(); // TODO new
        }
        return holder;
    }

    public Holder editHolder(String id, HolderDto dto) {
        Optional<Holder> optionalHolder = holderRepository.findById(id);
        Holder holderForUpdate = optionalHolder.orElseThrow(() -> new NullPointerException(this.HOLDERNOTFOUND));
        Holder updatedHolder = holderForUpdate.toBuilder().firstName(dto.getFirstName()).lastName(dto.getLastName())
                .email(dto.getEmail()).build();
        Holder savedHolder =  holderRepository.save(updatedHolder);
        borrowService.updateHolderInBorrow(updatedHolder);
        return savedHolder;
    }

    public boolean deleteHolder(String id) {
        Optional<Holder> optionalHolder = holderRepository.findById(id);
        Holder holderForDelete = optionalHolder.orElseThrow(() -> new NullPointerException(this.HOLDERNOTFOUND));
        try {
            holderRepository.delete(holderForDelete);
            borrowService.deleteHolderInBorrow(holderForDelete);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public HolderDetails convertToHolderDetails(Holder holder){
        return HolderDetails.builder().firstName(holder.getFirstName()).lastName(holder.getLastName())
                .email(holder.getEmail()).id(holder.getId())
                .build();
    }

}
