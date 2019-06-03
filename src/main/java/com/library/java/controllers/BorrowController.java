package com.library.java.controllers;

import com.library.java.Dto.BorrowDto;
import com.library.java.Dto.HolderDto;
import com.library.java.models.Borrow;
import com.library.java.services.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Secured(value = {"ROLE_ADMIN"})
@RestController
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    BorrowService borrowService;

    @GetMapping("/borrows")
    public String borrows() {
        return borrowService.getAllBorrow();
    }

    @PostMapping(path = "/addBorrow")
    public Borrow newBook(@RequestBody BorrowDto borrowDto) {
        return borrowService.addBorrow(borrowDto);
    }

    @PostMapping("/borrowsByHolder")
    public List<Borrow> getBooksByHolder(@RequestBody HolderDto holderDto){
        return borrowService.findByHolder(holderDto);
    }
}
