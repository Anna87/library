package com.library.java.services;

import com.library.java.converters.HolderConverter;
import com.library.java.dto.requests.HolderUpdateRequest;
import com.library.java.exceptions.NotFoundException;
import com.library.java.models.Holder;
import com.library.java.repositories.HolderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class HolderServiceTest {

    private HolderRepository holderRepository = mock(HolderRepository.class);

    private BorrowService borrowService = mock(BorrowService.class);

    private HolderConverter holderConverter = mock(HolderConverter.class);

    private HolderService holderService =
            new HolderService(holderRepository, borrowService, holderConverter);

    @Test
    public void shouldEditHolder() {

        List<String> bikeBrands = Arrays.asList("Giant", "Scott", "Trek", "GT");


        //given
        final HolderUpdateRequest givenHolderUpdateRequest = HolderUpdateRequest.builder()
                .firstName("givenFirstName")
                .firstName("givenLastName")
                .email("givenEmail")
                .build();

        final Holder givenHolder = Holder.builder()
                .firstName(givenHolderUpdateRequest.getFirstName())
                .lastName(givenHolderUpdateRequest.getLastName())
                .email(givenHolderUpdateRequest.getEmail())
                .build();

        Mockito.when(holderRepository.findById(any(String.class))).thenReturn(Optional.of(givenHolder));

        //when
        holderService.editHolder("",givenHolderUpdateRequest);

        //then
        final ArgumentCaptor<Holder> holderArgumentCaptor = ArgumentCaptor.forClass(Holder.class);
        verify(holderRepository,times(1)).save(holderArgumentCaptor.capture());

        Assert.assertEquals(givenHolderUpdateRequest.getFirstName(),holderArgumentCaptor.getValue().getFirstName());
        Assert.assertEquals(givenHolderUpdateRequest.getLastName(),holderArgumentCaptor.getValue().getLastName());
        Assert.assertEquals(givenHolderUpdateRequest.getEmail(),holderArgumentCaptor.getValue().getEmail());

        verify(borrowService,times(1)).updateHolderInHolder(holderArgumentCaptor.capture());

    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowException_whenHolderIsNotFoundOnEditHolder(){
        //when
        holderService.editHolder("notExistedId",null);
    }

    @Test
    public void shouldAddHolder(){
        //given
        final Holder givenHolder = Holder.builder().build();

        Mockito.when(holderConverter.convert(null)).thenReturn(givenHolder);

        //when
        holderService.addHolder(null);

        //then
        verify(holderRepository,times(1)).save(givenHolder);
    }

    @Test
    public void shouldDeleteHolder(){
        //given
        final Holder givenHolder = Holder.builder().build();

        Mockito.when(holderRepository.findById(any(String.class))).thenReturn(Optional.of(givenHolder));

        //when
        holderService.deleteHolder("notExistedId");

        //then
        verify(holderRepository,times(1)).delete(givenHolder);
        verify(borrowService,times(1)).deleteBorrowByHolderId(givenHolder.getId());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowException_whenHolderIsNotFoundOnFindById(){
        //when
        holderService.findById("notExistedId");
    }
}