package com.customer.java.services;

import com.customer.java.Dto.HolderDto;
import com.customer.java.common.JsonParserHelper;
import com.customer.java.models.Holder;
import com.customer.java.repositories.HolderRepository;
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

    public String GetAllHolders() {
        return jsonParserHelper.WriteToStrJson(holderRepository.findAll());
    }

    public Holder AddHolder(HolderDto dto) {
        return holderRepository.save(this.ConvertFromDto(dto));
    }

    private Holder ConvertFromDto(HolderDto dto) {
        Holder holder = Holder.builder().firstName(dto.getFirstName()).lastName(dto.getLastName()).build();
        if(!Objects.equals(dto.getId(),"")) {
            return holder.toBuilder().id(dto.getId()).build();
        }
        return holder;
    }

    public Holder editHolder(HolderDto dto) {
        Holder holder = this.ConvertFromDto(dto);
        Optional<Holder> optionalHolder = holderRepository.findById(holder.getId());
        Holder holderForUpdate = optionalHolder.orElseThrow(() -> new NullPointerException(this.HOLDERNOTFOUND));
        holderForUpdate.setFirstName(holder.getFirstName());
        holderForUpdate.setLastName(holder.getLastName());
        Holder savedHolder =  holderRepository.save(holderForUpdate);
        borrowService.updateHolderInBorrow(holderForUpdate);
        return savedHolder;
    }

    public boolean deleteHolder(HolderDto dto) {
        Holder holder = this.ConvertFromDto(dto);
        Optional<Holder> optionalHolder = holderRepository.findById(holder.getId());
        Holder holderForDelete = optionalHolder.orElseThrow(() -> new NullPointerException(this.HOLDERNOTFOUND));
        try {
            holderRepository.delete(holderForDelete);
            borrowService.deleteHolderInBorrow(holderForDelete);
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
