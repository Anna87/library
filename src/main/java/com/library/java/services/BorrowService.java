package com.library.java.services;

import com.google.common.collect.Lists;
import com.library.java.dto.requests.BorrowCreationRequest;
import com.library.java.exceptions.NotFoundException;
import com.library.java.models.Book;
import com.library.java.models.Borrow;
import com.library.java.models.Holder;
import com.library.java.repositories.BookRepository;
import com.library.java.repositories.BorrowRepository;
import com.library.java.repositories.HolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowService {
    private final String holderNotFound = "Holder not found";

    private final BorrowRepository borrowRepository;

    private final HolderRepository holderRepository;

    private final BookRepository bookRepository;

    @Transactional
    public Borrow addBorrow(final BorrowCreationRequest borrowCreationRequest) {
        final Holder holder = holderRepository.findById(borrowCreationRequest.getHolderId())
                .orElseThrow(() -> new NotFoundException(holderNotFound));
        final List<Book> books = Lists.newArrayList(bookRepository.findAllById(borrowCreationRequest.getBookIds()));

        final Borrow borrow = borrowRepository.save(Borrow.builder()
                .books(books)
                .holder(holder)
                .expiredDate(new Date(borrowCreationRequest.getExpiredDate()))
                .build());

        setBooksUnavailable(books);
        return borrow;
    }

    public List<Borrow> getAllBorrow() {
        return borrowRepository.findAll();
    }

    private void setBooksUnavailable(final List<Book> books) {
        Iterable<String> ids = books.stream().map(Book::getId).collect(Collectors.toList());
        Iterable<Book> foundBooks = bookRepository.findAllById(ids);
        for (Book item : foundBooks) {
            bookRepository.save(item.toBuilder().isAvailable(false).build());
        }
    }

    public void updateBookInBorrow(final Book updatedBook) {
        if (updatedBook.isAvailable()) {
            removeBookFromBorrows(updatedBook);
        } else {
            borrowRepository.findByBooksIdIn(updatedBook.getId()).forEach(
                    borrow -> {
                        borrow.getBooks().forEach(
                                book -> {
                                    if (book.getId().equals(updatedBook.getId())) {
                                        book.setAuthor(updatedBook.getAuthor());
                                        book.setTitle(updatedBook.getTitle());
                                    }
                                });
                        borrowRepository.save(borrow);
                    }
            );
        }
    }

    public void removeBookFromBorrows(final Book book) {
        borrowRepository.findByBooksIdIn(book.getId()).forEach(borrow -> {
            borrow.getBooks().removeIf(currBook -> currBook.getId().equals(book.getId()));
            if (borrow.getBooks().size() == 0) {
                borrowRepository.delete(borrow);
            } else {
                borrowRepository.save(borrow);
            }
        });
    }

    public void updateHolderInBorrow(final Holder holder) {
        Iterable<Borrow> borrows = borrowRepository.findByHolder(holder);
        for (Borrow item : borrows) {
            Borrow updatedBorrow = item.toBuilder().holder(holder).build();
            borrowRepository.save(updatedBorrow);
        }
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

    public List<String> getBooksByHolderId(@NotBlank String id) {
        return borrowRepository.findByHolderId(id).stream()
                .map(Borrow::getBooks)
                .flatMap(Collection::stream)
                .map(Book::getTitle)
                .collect(Collectors.toList());
    }
}
