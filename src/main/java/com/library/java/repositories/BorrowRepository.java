package com.library.java.repositories;

import com.library.java.models.Borrow;
import com.library.java.models.Holder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BorrowRepository extends MongoRepository<Borrow, String> {

    List<Borrow> findByHolder(Holder holder);

    List<Borrow> findByBooksIdIn(String id);

    List<Borrow> findByExpiredDateLessThanEqual(Date date);

    List<Borrow> findByHolderId(String id);
}
