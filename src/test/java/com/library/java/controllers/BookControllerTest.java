package com.library.java.controllers;

import com.library.java.ApplicationTest;
import com.library.java.converters.BookDetailsConverter;
import com.library.java.dto.requests.BookUpdateRequest;
import com.library.java.dto.responses.BookDetails;
import com.library.java.models.Book;
import com.library.java.security.CustomAuthenticationProvider;
import com.library.java.services.BookService;
import com.library.java.utils.FileUtils;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationTest.class, CustomAuthenticationProvider.class})
@Import({WebTestSecurityConfig.class})
@WebMvcTest(value = BookController.class)
public class BookControllerTest {
    private final String authorities = "authorities";
    private final String roleAdmin = "ROLE_ADMIN";
    private final String roleUser = "ROLE_USER";

    @MockBean
    private BookService bookService;

    @MockBean
    private BookDetailsConverter bookDetailsConverter;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void init() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    public void shouldReturnBookDetailsRequestingEditBook() {
        //given
        final String givenId = UUID.randomUUID().toString();
        final String givenAuthor = "givenAuthor";
        final BookUpdateRequest bookUpdateRequest = BookUpdateRequest.builder().title("any").author(givenAuthor).build();

        Mockito.when(bookService.editBook(givenId, bookUpdateRequest))
                .thenReturn(Book.builder().build());
        Mockito.when(bookDetailsConverter.convert(any(Book.class)))
                .thenReturn(BookDetails.builder()
                        .id(givenId)
                        .build());

        //then
        given()
                .body(bookUpdateRequest)
                .contentType(ContentType.JSON)
                .header(authorities, roleAdmin) //TODO not working without
                .when()
                .patch("/book/{id}/edit", givenId)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(OK.value())
                .body("id", equalTo(givenId))
                .body("author", equalTo(givenAuthor));

    }

    @Test
    public void shouldDeleteBookRequestingDeleteBook() {
        //given
        final String givenId = UUID.randomUUID().toString();

        Mockito.doNothing().when(bookService).deleteBook(givenId);

        //then
        given()
                .header(authorities, roleAdmin)
                .when()
                .delete("/book/{id}/delete", givenId)
                .then()
                .statusCode(OK.value());
    }
    @Test
    public void shouldReturnBookDetailsRequestingGetAllBook() {
        //given
        final List<Book> givenBooks = Arrays.asList();

        final List<BookDetails> givenBooksDetails = Arrays.asList();
        Mockito.when(bookService.getAll())
                .thenReturn(givenBooks);
        Mockito.when(bookDetailsConverter.convertList(givenBooks))
                .thenReturn(givenBooksDetails);

        //then
        given()
                .when()
                .get("/book")
                .then()
                .statusCode(OK.value());
    }

    @Test
    public void shouldReturnResourceRequestingDownloadBook() throws Exception {
        //given
        final String givenId = UUID.randomUUID().toString();

        final MockMultipartFile file = new MockMultipartFile("data", "filename.txt", "application/json", "{\"some\": \"xml\"}".getBytes());

        Mockito.when(bookService.downloadDigitalBook(givenId)).thenReturn(file);

        //then
        given()
                .header(authorities, roleAdmin)
                .header(authorities, roleUser)
                .when()
                .get("/book/{id}/download", givenId)
                .then()
                .statusCode(OK.value())
                .contentType(ContentType.JSON)
                .body("", Matchers.notNullValue());

    }

}
