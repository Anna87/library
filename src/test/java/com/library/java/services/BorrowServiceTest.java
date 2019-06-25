package com.library.java.services;

import com.google.common.collect.Lists;
import com.library.java.dto.requests.BorrowCreationRequest;
import com.library.java.exceptions.NotFoundException;
import com.library.java.models.Book;
import com.library.java.models.Borrow;
import com.library.java.models.Holder;
import com.library.java.repositories.BorrowRepository;
import groovy.lang.Lazy;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BorrowServiceTest {

    private BorrowRepository borrowRepository = mock(BorrowRepository.class);

    private HolderService holderService = mock(HolderService.class);

    private BookService bookService = mock(BookService.class);

    private BorrowService borrowService =
            new BorrowService(borrowRepository, holderService, bookService);

    @Test
    public void shouldAddBorrow() {
        //given
        final BorrowCreationRequest givenBorrowCreationRequest = getBorrowCreationRequest();

        final Holder givenHolder = Holder.builder()
                .id(givenBorrowCreationRequest.getHolderId())
                .build();

        final List<Book> givenBooks = Arrays.asList(
                Book.builder()
                        .id(givenBorrowCreationRequest.getBookIds().get(0))
                        .build(),
                Book.builder()
                        .id(givenBorrowCreationRequest.getBookIds().get(1))
                        .build());

        Mockito.when(bookService.findAllById(anyList())).thenReturn(givenBooks);

        Mockito.when(holderService.findById(any(String.class))).thenReturn(givenHolder);

        final ArgumentCaptor<Borrow> borrowArgumentCaptor = ArgumentCaptor.forClass(Borrow.class);

        //when
        borrowService.addBorrow(givenBorrowCreationRequest);

        //then
        verify(borrowRepository).save(borrowArgumentCaptor.capture());

        Assert.assertArrayEquals(givenBorrowCreationRequest.getBookIds().toArray(), borrowArgumentCaptor.getValue().getBooks().stream().map(Book::getId).toArray());
        Assert.assertEquals(givenBorrowCreationRequest.getHolderId(), borrowArgumentCaptor.getValue().getHolder().getId());
        Assert.assertEquals(givenBorrowCreationRequest.getExpiredDate(), new Long(borrowArgumentCaptor.getValue().getExpiredDate().getTime()));

        verify(bookService, times(1)).setBooksUnavailable(Lists.newArrayList(givenBooks));
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowException_whenBorrowIsNotFoundOnAddBorrow() {
        //given
        final BorrowCreationRequest givenBorrowCreationRequest = getBorrowCreationRequest();

        //when
        borrowService.addBorrow(givenBorrowCreationRequest);
    }

    @Test
    public void shouldUpdateBookInBorrow() {
        //given
        final Book givenUpdatedBook = getUpdatedBook(true);

        when(borrowRepository.findByBooksIdIn(any(String.class)))
                .thenReturn(
                        Lists.newArrayList(
                                Borrow.builder()
                                        .books(Lists.newArrayList(givenUpdatedBook))
                                        .build()
                        )
                );
        //when
        borrowService.updateBookInBorrow(givenUpdatedBook);

        //then
        verify(borrowRepository, times(1)).delete(any(Borrow.class));

    }

    @Test
    public void shouldUpdateBookInBorrow_whenBookIsNotAvailable() {
        //given
        final Book givenUpdatedBook = getUpdatedBook(false);

        final List<Borrow> givenBorrows = Arrays.asList(
                Borrow.builder()
                        .books(Arrays.asList(
                                Book.builder()
                                        .id("givenId")
                                        .author("otherAuthor")
                                        .title("otherTitle")
                                        .build()))
                        .build());

        Mockito.when(borrowRepository.findByBooksIdIn(any(String.class))).thenReturn(givenBorrows);

        //when
        borrowService.updateBookInBorrow(givenUpdatedBook);

        //then
        Assert.assertEquals(givenUpdatedBook.getAuthor(), givenBorrows.get(0).getBooks().get(0).getAuthor());
        Assert.assertEquals(givenUpdatedBook.getTitle(), givenBorrows.get(0).getBooks().get(0).getTitle());

        verify(borrowRepository, atLeastOnce()).save(givenBorrows.get(0));
    }

    @Test
    public void shouldRemoveBookFromBorrows(){
        //given
        final String givenBookId = UUID.randomUUID().toString();
        final List<Borrow> givenBorrows = Arrays.asList(
                Borrow.builder()
                        .books(Arrays.asList(
                                Book.builder()
                                        .id(givenBookId)
                                        .build()))
                        .build());

        Mockito.when(borrowService.findBorrowsByBookId(any(String.class))).thenReturn(givenBorrows);

        //when
        borrowService.removeBookFromBorrows(givenBookId);

        //then
        ArgumentCaptor<Borrow> argumentCaptor = ArgumentCaptor.forClass(Borrow.class);

        verify(borrowRepository,atLeastOnce()).delete(argumentCaptor.capture());
    }

    @Test
    public void shouldRemoveBookFromBorrows_whenSizeGtOne(){
        //given
        final String givenBookId = UUID.randomUUID().toString();

        final List<Borrow> givenBorrows = Arrays.asList(
                Borrow.builder()
                        .books(Arrays.asList(
                                Book.builder()
                                        .id(givenBookId)
                                        .build(),
                                Book.builder()
                                        .id("anotherId")
                                        .build()))
                        .build());

        Mockito.when(borrowService.findBorrowsByBookId(any(String.class))).thenReturn(givenBorrows);

        //when
        borrowService.removeBookFromBorrows(givenBookId);

        //then
        ArgumentCaptor<Borrow> argumentCaptor = ArgumentCaptor.forClass(Borrow.class);

        verify(borrowRepository,atLeastOnce()).save(argumentCaptor.capture());
    }

    @Test
    public void shouldUpdateHolderInBorrow() {
        //given
        final Holder givenHolder = getHolder();

        final List<Borrow> givenBorrows = getBorrows();

        Mockito.when(borrowRepository.findByHolderId(givenHolder.getId())).thenReturn(givenBorrows);

        //when
        borrowService.updateHolderInHolder(givenHolder);

        //then
        ArgumentCaptor<Borrow> argumentCaptor = ArgumentCaptor.forClass(Borrow.class);

        verify(borrowRepository,atLeastOnce()).save(argumentCaptor.capture());
        Assert.assertEquals(givenHolder.getFirstName(),argumentCaptor.getValue().getHolder().getFirstName());
        Assert.assertEquals(givenHolder.getLastName(),argumentCaptor.getValue().getHolder().getLastName());
        Assert.assertEquals(givenHolder.getEmail(),argumentCaptor.getValue().getHolder().getEmail());
    }

    @Test
    public void shouldDeleteHolderInBorrow() {
        //given
        final List<Borrow> givenBorrows = getBorrows();

        Mockito.when(borrowRepository.findByHolderId("")).thenReturn(givenBorrows);

        //when
        borrowService.deleteBorrowByHolderId("");

        //then
        verify(borrowRepository,atLeastOnce()).delete(givenBorrows.get(0));
    }

    private Book getUpdatedBook(boolean isAvailable) {
        return Book.builder()
                .id("givenId")
                .title("givenTitle")
                .author("givenAuthor")
                .isAvailable(isAvailable)
                .build();
    }

    private BorrowCreationRequest getBorrowCreationRequest() {
        return BorrowCreationRequest.builder()
                .holderId(UUID.randomUUID().toString())
                .bookIds(new ArrayList<>(Arrays.asList(UUID.randomUUID().toString(), UUID.randomUUID().toString())))
                .expiredDate(12345678910L)
                .build();
    }

    private Holder getHolder() {
        return Holder.builder()
                .id("givenId")
                .firstName("givenFirsName")
                .lastName("givenLastName")
                .email("givenEmail")
                .build();
    }

    private List<Borrow> getBorrows(){
        return Arrays.asList(
                Borrow.builder()
                        .holder(Holder.builder()
                                .firstName("anotherFirstName")
                                .lastName("anotherLastName")
                                .email("anotherEmail")
                                .build())
                        .build()
        );
    }
}