package com.library.java.controllers;

import com.library.java.ApplicationTest;
import com.library.java.converters.BookDetailsConverter;
import com.library.java.dto.requests.BookUpdateRequest;
import com.library.java.dto.responses.BookDetails;
import com.library.java.models.Book;
import com.library.java.security.CustomAuthenticationProvider;
import com.library.java.services.BookService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ApplicationTest.class})
@Import({WebTestSecurityConfig.class})
@WebMvcTest(value = BookController.class)
public class BookControllerTest {

    @MockBean
    private BookService bookService;

    @MockBean
    private BookDetailsConverter bookDetailsConverter;


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Before
    public void init() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    //@WithMockUser("ADMIN")
    public void shouldReturnBookDetailsRequestingEditBook() {
        //given
        final String author = "author";
        final String givenId = UUID.randomUUID().toString();
        final BookUpdateRequest bookUpdateRequest = BookUpdateRequest.builder().title("any").author("any").build();

        Mockito.when(bookService.editBook(givenId, bookUpdateRequest))
                .thenReturn(Book.builder().build());
        Mockito.when(bookDetailsConverter.convert(any(Book.class)))
                .thenReturn(BookDetails.builder()
                        .id(givenId)
                        .author(author)
                        .build());

        //then
        given()
                .body(bookUpdateRequest)
                .contentType(ContentType.JSON)
                .header("username", "any")
                .header("authorities", "ROLE_ADMIN")
                .when()
                .patch("/book/{id}/edit", givenId)
                .then()
                .contentType(ContentType.JSON)
                .statusCode(OK.value())
                .body("id", is(givenId))
                .body("author", is(author));
    }

}