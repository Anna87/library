package com.customer.java.client;


import com.customer.java.config.ClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(value = "storage", url = "http://localhost:8000", configuration = ClientConfiguration.class)
public interface StorageClient {

    @RequestMapping(method = RequestMethod.POST, value = "/addDigitalBook", consumes = "multipart/form-data")
    String addDigitalBook(@RequestPart("file") MultipartFile file, @RequestParam("title") String title, @RequestParam("author") String author);

    @RequestMapping(method = RequestMethod.POST, value = "/downloadDigitalBook")
    void downloadDigitalBook(@RequestParam("fileId") String fileId);

}

