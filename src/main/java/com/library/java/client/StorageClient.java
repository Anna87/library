package com.library.java.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@FeignClient("storage-service")
public interface StorageClient {

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/storage/add",
            consumes = "multipart/form-data"
    )
    String addDigitalBook(
            @NotNull @RequestPart("file") final MultipartFile file,
            @NotBlank @RequestParam("title") final String title,
            @NotBlank @RequestParam("author") final String author
    );

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/storage/download",
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE
    )
    MultipartFile downloadDigitalBook(@NotBlank @RequestParam("fileId") final String fileId);

}

