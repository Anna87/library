package com.library.java.controllers;

import com.library.java.converters.HolderDetailsConverter;
import com.library.java.dto.requests.HolderCreationRequest;
import com.library.java.dto.requests.HolderUpdateRequest;
import com.library.java.dto.responses.HolderDetails;
import com.library.java.services.BorrowService;
import com.library.java.services.HolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Secured(value = {"ROLE_ADMIN"})
@RestController
@RequestMapping("/holder")
@RequiredArgsConstructor
public class HolderController {

    private final HolderService holderService;

    private final HolderDetailsConverter holderDetailsConverter;

    private final BorrowService borrowService;

    @GetMapping
    public List<HolderDetails> getAll() {
        return holderDetailsConverter.convertList(holderService.getAllHolders());
    }

    @PostMapping(path = "/add")
    public HolderDetails newBook(@Valid @RequestBody final HolderCreationRequest dto) {
        return holderDetailsConverter.convert(holderService.addHolder(dto));
    }

    @PatchMapping("/{id}/edit")
    public HolderDetails editBook(@NotBlank @PathVariable("id") final String id, @Valid @RequestBody final HolderUpdateRequest holderUpdateRequest) {
        return holderDetailsConverter.convert(holderService.editHolder(id, holderUpdateRequest));
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteBook(@NotBlank @PathVariable("id") final String id) {
        holderService.deleteHolder(id);
    }

    @GetMapping("/{id}/books")
    public List<String> getBooksByHolderId(
            @NotBlank @PathVariable("id") final String id
    ) {
        return borrowService.getBooksByHolderId(id);
    }


}
