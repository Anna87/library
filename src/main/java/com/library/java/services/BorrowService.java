package com.library.java.services;

import com.library.java.dto.requests.BorrowCreationRequest;
import com.library.java.models.Book;
import com.library.java.models.Borrow;
import com.library.java.models.Holder;
import com.library.java.repositories.BorrowRepository;
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

    private final BorrowRepository borrowRepository;

    private final HolderService holderService;

    private final BookService bookService;

    @Transactional
    public Borrow addBorrow(final BorrowCreationRequest borrowCreationRequest) {
        final Holder holder = holderService.findById(borrowCreationRequest.getHolderId());
        final List<Book> books = bookService.findAllById(borrowCreationRequest.getBookIds());

        final Borrow borrow = borrowRepository.save(Borrow.builder()
                .books(books)
                .holder(holder)
                .expiredDate(new Date(borrowCreationRequest.getExpiredDate()))
                .build());

        bookService.setBooksUnavailable(books);
        return borrow;
    }

    public List<Borrow> getAllBorrow() {
        return borrowRepository.findAll();
    }

    public void updateBookInBorrow(final Book updatedBook) {
        if (updatedBook.isAvailable()) {
            removeBookFromBorrows(updatedBook.getId());
        } else {
            findBorrowsByBookId(updatedBook.getId()).forEach(
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

    public void removeBookFromBorrows(String id) {
        findBorrowsByBookId(id).forEach(borrow -> {
            java.util.ArrayList<Book> books = new java.util.ArrayList<>(borrow.getBooks());
            books.removeIf(currBook -> currBook.getId().equals(id));

            Borrow updatedBorrow = Borrow.builder()
                    .id(borrow.getId())
                    .holder(borrow.getHolder())
                    .books(books)
                    .expiredDate(borrow.getExpiredDate())
                    .build();
            if (updatedBorrow.getBooks().size() == 0) {
                borrowRepository.delete(borrow);
            } else {
                borrowRepository.save(updatedBorrow);
            }
        });
    }

    public List<Borrow> findBorrowsByBookId(String id){
        return borrowRepository.findByBooksIdIn(id);
    }


    public void updateHolderInHolder(final Holder holder) {
        borrowRepository.findByHolderId(holder.getId()).forEach(borrow ->
                borrowRepository.save(borrow.toBuilder().holder(holder).build()));
    }

    public void deleteBorrowByHolderId(final String id) {
        borrowRepository.findByHolderId(id).forEach(borrowRepository::delete);
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
