package com.library.java.services;

import com.library.java.ApplicationTest;
import com.library.java.client.StorageClient;
import com.library.java.models.Book;
import com.library.java.models.Borrow;
import com.library.java.models.Holder;
import com.library.java.repositories.BorrowRepository;
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
        BorrowService.class
})
public class BorrowServiceIT {
    @Autowired
    private BorrowService borrowService;

    @Autowired
    private BorrowRepository borrowRepository;

    @MockBean
    private BookService bookService;

    @MockBean
    private HolderService holderService;

    @MockBean
    private StorageClient storageClient;

    @Before
    public void init() {
        borrowRepository.deleteAll();
    }

    @Test
    public void shouldGetBooksByHolderId(){
        //given
        final String givenHolderId = UUID.randomUUID().toString();
        final Holder givenHolder = Holder.builder().id(givenHolderId).build();
        final Holder givenHolder2 = Holder.builder().id(UUID.randomUUID().toString()).build();

        final String givenTitle = "givenTitle";
        final String givenTitle2 = "givenTitle2";
        final String givenTitle3 = "givenTitle3";
        final Book givenBook = Book.builder().title(givenTitle).build();
        final Book givenBook2 = Book.builder().title(givenTitle2).build();
        final Book givenBook3 = Book.builder().title(givenTitle3).build();

        final Borrow givenBorrow1 = Borrow.builder()
                .holder(givenHolder)
                .books(Arrays.asList(givenBook))
                .build();

        final Borrow givenBorrow2 = Borrow.builder()
                .holder(givenHolder)
                .books(Arrays.asList(givenBook2))
                .build();

        final Borrow givenBorrow3 = Borrow.builder()
                .holder(givenHolder2)
                .books(Arrays.asList(givenBook3))
                .build();

        borrowRepository.save(givenBorrow1);
        borrowRepository.save(givenBorrow2);
        borrowRepository.save(givenBorrow3);

        final List<String> expectedTitles = Arrays.asList(givenTitle,givenTitle2);

        //when
        final List<String> result = borrowService.getBooksByHolderId(givenHolderId);
        //then
        assertThat(expectedTitles).isEqualTo(result);
    }

    @Test
    public void shouldGetExpiredBorrow() {
        //given
        final Borrow givenBorrow1 = Borrow.builder()
                .expiredDate(tomorrow())
                .build();
        final Borrow givenBorrow2 = Borrow.builder()
                .expiredDate(yesterday())
                .build();
        final Borrow givenBorrow3 = Borrow.builder()
                .expiredDate(oneYearAgo())
                .build();

        borrowRepository.save(givenBorrow1);
        borrowRepository.save(givenBorrow2);
        borrowRepository.save(givenBorrow3);
        List<Borrow> expectedBorrows = Arrays.asList(givenBorrow2,givenBorrow3);

        //when
        List<Borrow> result = borrowService.getExpiredBorrow(today());

        //then
        assertThat(expectedBorrows).isEqualTo(result);

    }

    @Test
    public void shouldDeleteBorrowByHolderId(){
        //given
        final String givenHolderId = UUID.randomUUID().toString();
        final Holder givenHolder = Holder.builder().id(givenHolderId).build();
        final Holder givenHolder2 = Holder.builder().id(UUID.randomUUID().toString()).build();

        final Borrow givenBorrow1 = Borrow.builder()
                .holder(givenHolder)
                .build();
        final Borrow givenBorrow2 = Borrow.builder()
                .holder(givenHolder)
                .build();

        final Borrow givenBorrow3 = Borrow.builder()
                .holder(givenHolder2)
                .build();

        borrowRepository.save(givenBorrow1);
        borrowRepository.save(givenBorrow2);
        borrowRepository.save(givenBorrow3);

        final List<Borrow> expectedBorrows = Arrays.asList(givenBorrow3);

        //when
        borrowService.deleteBorrowByHolderId(givenHolderId);
        //then
        assertThat(expectedBorrows).isEqualTo(borrowRepository.findAll());

    }

    @Test
    public void shouldUpdateHolderInBorrow(){
        //given
        final String givenHolderId = UUID.randomUUID().toString();
        final Holder oldHolder = Holder.builder().id(givenHolderId)
                .firstName("oldFirstName")
                .lastName("oldLastName")
                .email("oldEmail")
                .build();
        final Holder newHolder = Holder.builder().id(givenHolderId)
                .firstName("updatedFirstName")
                .lastName("updatedLastName")
                .email("updatedEmail")
                .build();
        final Borrow givenBorrow = Borrow.builder()
                .holder(oldHolder)
                .build();
        borrowRepository.save(givenBorrow);

        final Borrow updatedBorrow = givenBorrow.toBuilder()
                .holder(newHolder)
                .build();

        final List<Borrow> expectedBorrows = Arrays.asList(updatedBorrow );
        //when
        borrowService.updateHolderInHolder(newHolder);

        assertThat(expectedBorrows).isEqualTo(borrowRepository.findByHolderId(givenHolderId));
    }

    @Test
    public void shouldFindBorrowsByBookId(){
        //given
        final String givenBookId = UUID.randomUUID().toString();
        final Book givenBook = Book.builder()
                .id(givenBookId)
                .title("givenTitle")
                .author("givenAuthor")
                .build();
        final Borrow givenBorrow = Borrow.builder()
                .books(Arrays.asList(givenBook))
                .build();
        final Borrow givenBorrow2 = Borrow.builder()
                .books(Arrays.asList(Book.builder()
                        .id(UUID.randomUUID().toString())
                        .title("anotherTitle")
                        .author("anotherAuthor")
                        .build()))
                .build();
        borrowRepository.save(givenBorrow);
        borrowRepository.save(givenBorrow2);
        //when
        List<Borrow> result = borrowService.findBorrowsByBookId(givenBookId);
        //then
        assertThat(Arrays.asList(givenBorrow)).isEqualTo(result);

    }


    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private Date tomorrow() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    private Date oneYearAgo() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        return cal.getTime();
    }

    private Date today() {
        final Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

}
