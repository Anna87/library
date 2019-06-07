package com.library.java.services;

import com.google.common.collect.Lists;
import com.library.java.Dto.BorrowDto;
import com.library.java.Dto.responses.BorrowDetails;
import com.library.java.common.JsonParserHelper;
import com.library.java.models.Book;
import com.library.java.models.Borrow;
import com.library.java.models.Holder;
import com.library.java.repositories.BookRepository;
import com.library.java.repositories.BorrowRepository;
import com.library.java.repositories.HolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BorrowService {
    private final String HOLDERNOTFOUND = "Holder not found";
    @Autowired
    BorrowRepository borrowRepository;
    @Autowired
    HolderRepository holderRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    JsonParserHelper jsonParserHelper;

    public Borrow addBorrow(BorrowDto borrowDto) {
        Borrow borrow = borrowRepository.save(this.convertFromDto(borrowDto));

        this.setBooksUnavalible(borrowDto.getBooks());

        return borrow;
    }

    public String getAllBorrow() {
        return jsonParserHelper.writeToStrJson(borrowRepository.findAll());
    }

    public List<Borrow> findByHolder(String id){
        Optional<Holder> optionalHolder = holderRepository.findById(id);
        Holder holder = optionalHolder.orElseThrow(() -> new NullPointerException(this.HOLDERNOTFOUND));
        return borrowRepository.findByHolder(holder);
    }

    private Borrow convertFromDto(BorrowDto dto) {
        return Borrow.builder().books(Lists.newArrayList(dto.getBooks())).holder(dto.getHolder()).expiredDate(new Date(dto.getExpiredDate())).build();
    }

    private void setBooksUnavalible(Book[] books){
        Iterable<String> ids = Arrays.stream(books).map(s -> s.getId()).collect(Collectors.toList());
        Iterable<Book> foundBooks = bookRepository.findAllById(ids);
        for (Book item : foundBooks) {
            bookRepository.save(item.toBuilder().isAvalible(false).build()); // ??? TODO bookRepository.saveAll
        }
    }

    public BorrowDetails convertToBorrowDetails(Borrow borrow){
        return BorrowDetails.builder().id(borrow.getId()).books(borrow.getBooks())
                .holder(borrow.getHolder()).build();
    }
    public List<BorrowDetails> convertToBorrowDetails(List<Borrow> borrows){
        return borrows.stream().map(borrow ->
                BorrowDetails.builder().id(borrow.getId()).books(borrow.getBooks()).holder(borrow.getHolder()).build())
                .collect(Collectors.toList());
    }

    public void updateBookInBorrow(Book book) {
        Iterable<Borrow> borrows = borrowRepository.findByBooksIdIn(book.getId());
        for (Borrow borrow : borrows) {
            for (Book b : borrow.books) {
                if (!Objects.equals(book.getId(),b.getId())) return;

                if(book.getIsAvalible()){
                    removeBookFromBorrow(borrows, borrow,b);
                }else {
                    bookRepository.save(b.toBuilder().title(book.getTitle()).autor(book.getAutor()).build()); // ??? TODO bookRepository.saveAll
                }
                break;
            }
            break;
        }
    }

    private void removeBookFromBorrow(Iterable<Borrow> borrows, Borrow borrow, Book book){
        borrow.books.remove(book);
        if(borrow.books.size() == 0){
            Iterator<Borrow> i = borrows.iterator();
            while (i.hasNext()) {
                Borrow s = i.next();
                if (s.equals(borrow)) {
                    i.remove();
                    break;
                }
            }
        }
        borrowRepository.delete(borrow);
    }

    public void updateHolderInBorrow(Holder holder) {
        Iterable<Borrow> borrows = borrowRepository.findByHolder(holder);
        for (Borrow item : borrows) {
            Borrow updatedBorrow = item.toBuilder().holder(holder).build();
            borrowRepository.save(updatedBorrow);
        }
    }

    public void deleteBookInBorrow(Book book) {
        Iterable<Borrow> borrows = borrowRepository.findByBooksIdIn(book.getId());
        for (Borrow item : borrows) {
            for (Book b : item.books) {
                if (!Objects.equals(book.getId(),b.getId())) return;
                item.books.remove(b);
                break;
            }
        }
        borrowRepository.saveAll(borrows);
    }

    public void deleteHolderInBorrow(Holder holder) {
        Iterable<Borrow> borrows = borrowRepository.findByHolder(holder);
        for (Borrow item : borrows) {
            borrowRepository.delete(item);
        }
    }

    public List<Borrow> getExpiredBorrow(Date date){

        return borrowRepository.findByExpiredDateLessThanEqual(date);
    }
}
