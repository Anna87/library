package com.customer.java.controllers;

import com.customer.java.Dto.BookDto;
import com.customer.java.Dto.HolderDto;
import com.customer.java.models.Book;
import com.customer.java.models.Holder;
import com.customer.java.services.HolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolderController {

    @Autowired
    HolderService holderService;

    @GetMapping("/holders")
    public String holders() {
        return holderService.GetAllHolders();
    }

    @PostMapping(path = "/addHolder")
    public Holder newBook(@RequestBody HolderDto dto) {
        return holderService.AddHolder(dto);
    }

    @PostMapping(path = "/editHolder")
    public Holder editBook(@RequestBody HolderDto dto) {
        return holderService.editHolder(dto);
    }

    @PostMapping(path = "/deleteHolder")
    public boolean deleteBook(@RequestBody HolderDto dto) {
        return holderService.deleteHolder(dto);
    }

}
