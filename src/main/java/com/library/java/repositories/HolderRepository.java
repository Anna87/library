package com.library.java.repositories;


import com.library.java.models.Holder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolderRepository extends MongoRepository<Holder, String> {
    List<Holder> findByLastName(String lastName);
}
