package com.library.java.services;

import com.library.java.converters.HolderConverter;
import com.library.java.dto.requests.HolderCreationRequest;
import com.library.java.dto.requests.HolderUpdateRequest;
import com.library.java.exceptions.NotFoundException;
import com.library.java.models.Holder;
import com.library.java.repositories.HolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolderService {
    private final String holderNotFound = "Holder not found";

    private final HolderRepository holderRepository;

    private final BorrowService borrowService;

    private final HolderConverter holderConverter;

    public List<Holder> getAllHolders() {
        return holderRepository.findAll();
    }

    public Holder addHolder(final HolderCreationRequest dto) {
        return holderRepository.save(holderConverter.convert(dto));
    }

    @Transactional
    public Holder editHolder(final String id, final HolderUpdateRequest holderUpdateRequest) {
        final Holder holderForUpdate =
                holderRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(holderNotFound));

        final Holder updatedHolder = holderForUpdate.toBuilder()
                .firstName(holderUpdateRequest.getFirstName())
                .lastName(holderUpdateRequest.getLastName())
                .email(holderUpdateRequest.getEmail())
                .build();

        final Holder savedHolder =  holderRepository.save(updatedHolder);

        borrowService.updateHolderInHolder(updatedHolder);

        return savedHolder;
    }

    @Transactional
    public void deleteHolder(final String id) {
        final Holder holder = findById(id);
        holderRepository.delete(holder);
        borrowService.deleteBorrowByHolderId(holder.getId());
    }

    public Holder findById(String id){
        final Holder holder = holderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(holderNotFound));
        return holder;
    }

}
