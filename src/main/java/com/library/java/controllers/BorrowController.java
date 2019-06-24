package com.library.java.controllers;

import com.library.java.dto.requests.BorrowCreationRequest;
import com.library.java.dto.responses.BorrowDetails;
import com.library.java.converters.BorrowDetailsConverter;
import com.library.java.services.BorrowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.library.java.constants.Constants.ROLE_ADMIN;

@Secured(value = {ROLE_ADMIN})
@RestController
@RequiredArgsConstructor
@RequestMapping("/borrow")
public class BorrowController {

    private final BorrowService borrowService;
    private final BorrowDetailsConverter borrowDetailsConverter;

    @GetMapping
    public List<BorrowDetails> getAll() {
        return borrowDetailsConverter.convertList(borrowService.getAllBorrow());
    }

    @PostMapping(path = "/add")
    public BorrowDetails newBook(@Valid @RequestBody final BorrowCreationRequest borrowCreationRequest) {
        return borrowDetailsConverter.convert(borrowService.addBorrow(borrowCreationRequest));
    }
}
