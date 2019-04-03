package com.customer.java.services;

import com.customer.java.Dto.BorrowDto;
import com.customer.java.Dto.HolderDto;
import com.customer.java.common.JsonParserHelper;
import com.customer.java.models.Book;
import com.customer.java.models.Borrow;
import com.customer.java.models.Holder;
import com.customer.java.repositories.BookRepository;
import com.customer.java.repositories.BorrowRepository;
import com.customer.java.repositories.HolderRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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

    public Borrow AddBorrow(BorrowDto borrowDto) {
        Borrow borrow = borrowRepository.save(this.ConvertFromDto(borrowDto));

        this.SetBooksUnavalible(borrowDto.getBooks());

        return borrow;
    }

    public String GetAllBorrow() {
        return jsonParserHelper.WriteToStrJson(borrowRepository.findAll());
    }

    public List<Borrow> findByHolder(HolderDto dto){
        Holder holder = holderRepository.findByLastName(dto.getLastName()).get(0);
        return borrowRepository.findByHolder(holder);
    }

    private Borrow ConvertFromDto(BorrowDto dto) {
        return Borrow.builder().books(Lists.newArrayList(dto.getBooks())).holder(dto.getHolder()).build();
    }

    private void SetBooksUnavalible(Book[] books){
        Iterable<String> ids = Arrays.stream(books).map(s -> s.getId()).collect(Collectors.toList());
        Iterable<Book> foundBooks = bookRepository.findAllById(ids);
        for (Book item : foundBooks) {
            item.setIsAvalible(false);
        }
        bookRepository.saveAll(foundBooks);
    }

    public void updateBookInBorrow(Book book) {
        Iterable<Borrow> borrows = borrowRepository.findByBooksIdIn(book.getId());
        for (Borrow borrow : borrows) {
            for (Book b : borrow.books) {
                if (!Objects.equals(book.getId(),b.getId())) return;

                if(book.getIsAvalible()){
                    removeBookFromBorrow(borrows, borrow,b);
                }else {
                    b.setTitle(book.getTitle());
                    b.setAutor(book.getAutor());
                }
                break;
            }
            break;
        }
        borrowRepository.saveAll(borrows);
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
            item.holder.setFirstName(holder.getFirstName());
            item.holder.setLastName(holder.getLastName());
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
}
