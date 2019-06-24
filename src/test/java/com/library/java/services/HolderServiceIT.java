package com.library.java.services;

import com.library.java.ApplicationTest;
import com.library.java.client.StorageClient;
import com.library.java.converters.HolderConverter;
import com.library.java.models.Holder;
import com.library.java.repositories.HolderRepository;
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

import java.util.UUID;

@RunWith(SpringRunner.class)
@DataMongoTest
@ContextConfiguration(classes = ApplicationTest.class)
@Import({
        HolderService.class
})
public class HolderServiceIT {

    @Autowired
    private HolderService holderService;

    @Autowired
    private HolderRepository holderRepository;

    @MockBean
    private BorrowService borrowService;

    @MockBean
    private HolderConverter holderConverter;

    @MockBean
    private StorageClient storageClient;

    @Before
    public void init() {
        holderRepository.deleteAll();
    }

    @Test
    public void shouldReturnHolderById() {
        //given
        final String givenId = UUID.randomUUID().toString();
        final Holder givenHolder = Holder.builder().id(givenId).build();
        final Holder givenHolder2 = Holder.builder().id(UUID.randomUUID().toString()).build();

        holderRepository.save(givenHolder);
        holderRepository.save(givenHolder2);
        //when
        Holder result = holderService.findById(givenId);
        //then
        Assert.assertEquals(givenHolder, result);
    }
}
