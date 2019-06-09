package com.library.java.controllers;

import com.library.java.Dto.BorrowDto;
import com.library.java.Dto.responses.BorrowDetails;
import com.library.java.converters.BorrowDetailsConverter;
import com.library.java.services.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Secured(value = {"ROLE_ADMIN"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/borrow")
public class BorrowController {

    private final BorrowService borrowService;
    private final BorrowDetailsConverter borrowDetailsConverter;

    @GetMapping
    public String getAll() {
        return borrowService.getAllBorrow();
    }

    @PostMapping(path = "/add")
    public BorrowDetails newBook(@Valid @RequestBody final BorrowDto borrowDto) {
        return borrowDetailsConverter.convert(borrowService.addBorrow(borrowDto));
    }

    @GetMapping("/{id}/getBorrowsByHolderId") // TODO ??? getByHolderId
    public List<BorrowDetails> getBorrowsByHolder(@NotBlank @PathVariable("id") final String id){
        return borrowDetailsConverter.convertList(borrowService.findByHolder(id));
    }
}
