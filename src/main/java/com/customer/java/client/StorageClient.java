package com.customer.java.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

//@FeignClient(value = "storage", url = "http://localhost:8000", configuration = ClientConfiguration.class)
//@FeignClient(value = "storage-service",/* url = "http://storage-service",*/ configuration = ClientConfiguration.class)
@FeignClient("storage-service")
public interface StorageClient {

    @RequestMapping(method = RequestMethod.POST, value = "/storage/addDigitalBook", consumes = "multipart/form-data")
    String addDigitalBook(@RequestPart("file") MultipartFile file, @RequestParam("title") String title, @RequestParam("author") String author);

    //@ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/storage/downloadDigitalBook", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    MultipartFile downloadDigitalBook(@RequestParam("fileId") String fileId);

}

