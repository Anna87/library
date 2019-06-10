package com.library.java.repositories;


import com.library.java.models.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    List<Book> findByTitle(String title);

    //List<Book> findByIdsIn(String[] ids); // TODO check working
}

