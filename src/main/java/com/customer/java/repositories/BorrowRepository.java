package com.customer.java.repositories;

import com.customer.java.models.Borrow;
import com.customer.java.models.Holder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BorrowRepository extends MongoRepository<Borrow, String> {
    List<Borrow> findByHolder(Holder holder);

    List<Borrow> findByBooksIdIn(String id);

    //List<Borrow> findByExpiredDateBefore(Date date);

    List<Borrow> findByExpiredDateLessThanEqual(Date date);
}
