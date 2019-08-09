package com.library.java.services;

import com.library.java.client.StorageClient;
import com.library.java.converters.BookConverter;
import com.library.java.dto.requests.BookCreationRequest;
import com.library.java.dto.requests.BookUpdateRequest;
import com.library.java.exceptions.NotFoundException;
import com.library.java.models.Book;
import com.library.java.repositories.BookRepository;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    private BookRepository bookRepository = mock(BookRepository.class);

    private BorrowService borrowService = mock(BorrowService.class);

    private StorageClient storageClient = mock(StorageClient.class);

    private BookConverter bookConverter = mock(BookConverter.class);

    private MultipartFile multipartFile = mock(MultipartFile.class);

    private BookService bookService =
            new BookService(bookRepository, borrowService, storageClient, bookConverter);

    @Test
    public void shouldEditBook() {
        //given
        final Book givenBook = Book.builder()
                .id("randomId")
                .title("givenTitle")
                .author("givenAuthor")
                .isAvailable(true)
                .build();

        final BookUpdateRequest givenUpdateRequest = getBookUpdateRequest();

        Mockito.when(bookRepository.findById(any(String.class))).thenReturn(Optional.of(givenBook));

        //when
        bookService.editBook(givenBook.getId(), givenUpdateRequest);

        //then
        final ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookArgumentCaptor.capture());

        Assert.assertEquals(givenUpdateRequest.getTitle(), bookArgumentCaptor.getValue().getTitle());
        Assert.assertEquals(givenUpdateRequest.getAuthor(), bookArgumentCaptor.getValue().getAuthor());
        Assert.assertTrue(bookArgumentCaptor.getValue().isAvailable());

        verify(borrowService, times(1)).updateBookInBorrow(bookArgumentCaptor.getValue());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowException_whenBookIsNotFound() {
        //when
        bookService.findById("notExistedId");
    }

    @Test
    public void shouldAddBook() {
        //given
        final Book givenBook = Book.builder()
                .id("randomId")
                .title("givenTitle")
                .author("givenAuthor")
                .hasDigitalFormat(false)
                .isAvailable(true)
                .build();

        Mockito.when(bookConverter.convert(any())).thenReturn(givenBook);
        //when
        bookService.addBook(null, any(BookCreationRequest.class));
        //than
        verify(bookRepository, times(1)).save(givenBook);
        Assert.assertFalse(givenBook.isHasDigitalFormat());
    }

    @Test
    public void shouldAddBookWithMultipartFile() {
        //given
        BookCreationRequest givenBookCreationRequest = BookCreationRequest.builder()
                .fileName("givenFileName")
                .hasDigitalFormat(true)
                .build();

        //given
        final Book givenBook = Book.builder()
                .id("randomId")
                .title("givenTitle")
                .author("givenAuthor")
                .hasDigitalFormat(false)
                .isAvailable(true)
                .build();

        StringBuilder stringBuilder = new StringBuilder();
        final String givenFileId = "randomFileId";

        Mockito.when(bookConverter.convert(givenBookCreationRequest)).thenReturn(givenBook);

        Mockito.when(storageClient.addDigitalBook(multipartFile, givenBookCreationRequest.getTitle(), givenBookCreationRequest.getAuthor()))
                .thenReturn(givenFileId);
        //when
        bookService.addBook(multipartFile, givenBookCreationRequest);
        //then
        verify(storageClient, times(1))
                .addDigitalBook(
                        multipartFile,
                        givenBookCreationRequest.getTitle(),
                        givenBookCreationRequest.getAuthor()
                );

        verify(bookRepository, times(1)).save(givenBook);

        Assert.assertTrue(givenBook.isHasDigitalFormat());
        Assert.assertEquals(givenFileId, givenBook.getFileId());
        Assert.assertEquals(givenBookCreationRequest.getFileName(), givenBook.getFileName());

    }

    @Test
    public void shouldDeleteBook(){
        //given
        final String givenBookId = UUID.randomUUID().toString();
        final Book givenBook = Book.builder().id(givenBookId).build();

        Mockito.when(bookRepository.findById(any(String.class))).thenReturn(Optional.of(givenBook));

        //when
        bookService.deleteBook("");

        //then
        verify(bookRepository,times(1)).delete(givenBook);
        verify(borrowService,times(1)).removeBookFromBorrows(givenBookId);
    }

    @Test
    public void shouldDownloadDigitalBook() throws IOException{
        //when
        bookService.downloadDigitalBook("notExistedId");

        //then
        verify(storageClient,times(1)).downloadDigitalBook("");
    }

    @Test
    public void shouldSetBooksUnavailable(){
        //given
        final List<Book> books = Arrays.asList(
                Book.builder()
                .id("givenId")
                .isAvailable(true)
                .build());

        //when
        bookService.setBooksUnavailable(books);

        //then
        ArgumentCaptor<Book> argumentCaptor = ArgumentCaptor.forClass(Book.class);

        verify(bookRepository,atLeastOnce()).save(argumentCaptor.capture());
        Assert.assertFalse(argumentCaptor.getValue().isAvailable());

    }

    private BookUpdateRequest getBookUpdateRequest() {
        return BookUpdateRequest.builder()
                .title("newTitle")
                .author("newAuthor")
                .isAvailable(true)
                .build();
    }
}