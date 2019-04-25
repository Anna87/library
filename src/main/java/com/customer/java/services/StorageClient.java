package com.customer.java.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "http://localhost:8000")
public interface StorageClient {
    /*@RequestMapping(method = RequestMethod.POST, value = "/addDigitalBook2", consumes = "application/json")
    String AddDigitalBook(@PathVariable("title") String title);*/
    /*
    @RequestLine("POST /addDigitalBook2")
    String AddDigitalBook(@Param("title") String title);*/

    @PostMapping(value = "/addDigitalBook2",  consumes = "application/json")
    String AddDigitalBook(@PathVariable("title") String title);
}

