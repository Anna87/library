package com.customer.java.controllers;

import com.customer.java.Dto.BorrowDto;
import com.customer.java.Dto.HolderDto;
import com.customer.java.models.Borrow;
import com.customer.java.services.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BorrowController {
    @Autowired
    BorrowService borrowService;

    @GetMapping("/borrows")
    public String borrows() {
        return borrowService.GetAllBorrow();
    }

    @PostMapping(path = "/addBorrow")
    public Borrow newBook(@RequestBody BorrowDto borrowDto) {
        return borrowService.AddBorrow(borrowDto);
    }

    @PostMapping("/borrowsByHolder")
    public List<Borrow> getBooksByHolder(@RequestBody HolderDto holderDto){
        return borrowService.findByHolder(holderDto);
    }
}
