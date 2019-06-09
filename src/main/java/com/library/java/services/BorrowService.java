package com.library.java.services;

import com.library.java.Dto.BorrowDto;
import com.library.java.common.JsonParserHelper;
import com.library.java.converters.BorrowConverter;
import com.library.java.models.Book;
import com.library.java.models.Borrow;
import com.library.java.models.Holder;
import com.library.java.repositories.BookRepository;
import com.library.java.repositories.BorrowRepository;
import com.library.java.repositories.HolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final String HOLDERNOTFOUND = "Holder not found";

    private final BorrowRepository borrowRepository;

    private final HolderRepository holderRepository;

    private final BookRepository bookRepository;

    private final JsonParserHelper jsonParserHelper;

    private final BorrowConverter borrowConverter;

    public Borrow addBorrow(BorrowDto borrowDto) {
        Borrow borrow = borrowRepository.save(borrowConverter.convert(borrowDto));
        this.setBooksUnavalible(borrowDto.getBooks());
        return borrow;
    }

    public String getAllBorrow() {
        return jsonParserHelper.writeToStrJson(borrowRepository.findAll());
    }

    public List<Borrow> findByHolder(final String id) {
        Optional<Holder> optionalHolder = holderRepository.findById(id);
        Holder holder = optionalHolder.orElseThrow(() -> new NullPointerException(this.HOLDERNOTFOUND));
        return borrowRepository.findByHolder(holder);
    }

    private void setBooksUnavalible(final Book[] books) {
        Iterable<String> ids = Arrays.stream(books).map(s -> s.getId()).collect(Collectors.toList());
        Iterable<Book> foundBooks = bookRepository.findAllById(ids);
        for (Book item : foundBooks) {
            bookRepository.save(item.toBuilder().isAvalible(false).build());
        }
    }


    public void updateBookInBorrow(final Book book) {
        Iterable<Borrow> borrows = borrowRepository.findByBooksIdIn(book.getId());
        for (Borrow borrow : borrows) {
            for (Book b : borrow.getBooks()) {
                if (!Objects.equals(book.getId(), b.getId())) return;

                if (book.getIsAvalible()) {
                    removeBookFromBorrow(borrows, borrow, b);
                } else {
                    bookRepository.save(b.toBuilder().title(book.getTitle()).autor(book.getAutor()).build());
                }
                break;
            }
            break;
        }
    }

    private void removeBookFromBorrow(final Iterable<Borrow> borrows, final Borrow borrow, final Book book) {
        borrow.getBooks().remove(book);
        if (borrow.getBooks().size() == 0) {
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

    public void updateHolderInBorrow(final Holder holder) {
        Iterable<Borrow> borrows = borrowRepository.findByHolder(holder);
        for (Borrow item : borrows) {
            Borrow updatedBorrow = item.toBuilder().holder(holder).build();
            borrowRepository.save(updatedBorrow);
        }
    }

    public void deleteBookInBorrow(final Book book) {
        Iterable<Borrow> borrows = borrowRepository.findByBooksIdIn(book.getId());
        for (Borrow item : borrows) {
            for (Book b : item.getBooks()) {
                if (!Objects.equals(book.getId(), b.getId())) return;
                item.getBooks().remove(b);  // TODO check working
                break;
            }
        }
        borrowRepository.saveAll(borrows);
    }

    public void deleteHolderInBorrow(final Holder holder) {
        Iterable<Borrow> borrows = borrowRepository.findByHolder(holder);
        for (Borrow item : borrows) {
            borrowRepository.delete(item);
        }
    }

    public List<Borrow> getExpiredBorrow(final Date date) {
        return borrowRepository.findByExpiredDateLessThanEqual(date);
    }
}
