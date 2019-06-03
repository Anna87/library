package com.customer.java.controllers;

import com.customer.java.Dto.HolderDto;
import com.customer.java.models.Holder;
import com.customer.java.services.HolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Secured(value = {"ROLE_ADMIN"})
@RestController
@RequestMapping("/holder")
public class HolderController {

    @Autowired
    HolderService holderService;

    @GetMapping("/holders")
    public String holders() {
        return holderService.getAllHolders();
    }

    @PostMapping(path = "/addHolder")
    public Holder newBook(@RequestBody HolderDto dto) {
        return holderService.addHolder(dto);
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
