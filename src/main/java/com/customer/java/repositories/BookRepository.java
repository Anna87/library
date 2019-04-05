package com.customer.java.repositories;


import com.customer.java.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
//    @Query(value = "{ 'title' : ?0}")
    List<Book> findByTitle(String title);
}
