package com.library.java.services;

import com.library.java.ApplicationTest;
import com.library.java.client.StorageClient;
import com.library.java.converters.BookConverter;
import com.library.java.models.Book;
import com.library.java.repositories.BookRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
@ContextConfiguration(classes = ApplicationTest.class)
@Import({
        BookService.class
})
public class BookServiceIT {
    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    @MockBean
    private BorrowService borrowService;

    @MockBean
    private StorageClient storageClient;

    @MockBean
    private BookConverter bookConverter;

    @Before
    public void init() {
        bookRepository.deleteAll();
    }

    @Test
    public void shouldReturnBooksByIds(){
        //given
        final String givenId = UUID.randomUUID().toString();
        final String givenId2 = UUID.randomUUID().toString();

        final Book givenBook = Book.builder().id(givenId).build();
        final Book givenBook2 = Book.builder().id(givenId2).build();

        bookRepository.save(givenBook);
        bookRepository.save(givenBook2);

        final List<Book> givenBooks = Arrays.asList(givenBook,givenBook2);
        final List<String> ids = Arrays.asList(givenId,givenId2);

        //when
        List<Book> result = bookService.findAllById(ids);

        //then
        assertThat(givenBooks).isEqualTo(result);
    }

    @Test
    public void shouldReturnBookById(){
        //given
        final String givenId = UUID.randomUUID().toString();
        final String givenId2 = UUID.randomUUID().toString();

        final Book givenBook = Book.builder().id(givenId).build();
        final Book givenBook2 = Book.builder().id(givenId2).build();

        bookRepository.save(givenBook);
        bookRepository.save(givenBook2);

        //when
        final Book result = bookService.findById(givenId);

        //then
        Assert.assertEquals(givenBook,result);
    }

    @Test
    public void shouldSetBupdatedBooksookUnavailable(){
        //given
        final String givenId = UUID.randomUUID().toString();
        final String givenId2 = UUID.randomUUID().toString();
        final Book givenBook = Book.builder()
                .id(givenId)
                .isAvailable(true)
                .build();
        final Book givenBook2 = Book.builder()
                .id(givenId2)
                .isAvailable(true)
                .build();

        bookRepository.save(givenBook);
        bookRepository.save(givenBook2);
        final List<Book> givenBooks = Arrays.asList(givenBook,givenBook2);

        final List<Book> updatedBooks =
                Arrays.asList(
                        givenBook.toBuilder()
                        .isAvailable(false)
                        .build(),
                        givenBook2.toBuilder()
                        .isAvailable(false)
                        .build());
        //when
        bookService.setBooksUnavailable(givenBooks);

        assertThat(updatedBooks).isEqualTo(bookRepository.findAll());


    }

}
