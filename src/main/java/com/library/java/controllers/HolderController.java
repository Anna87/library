package com.library.java.controllers;

import com.library.java.Dto.HolderDto;
import com.library.java.Dto.requests.HolderUpdateRequest;
import com.library.java.Dto.responses.HolderDetails;
import com.library.java.converters.HolderDetailsConverter;
import com.library.java.services.HolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Secured(value = {"ROLE_ADMIN"})
@RestController
@RequestMapping("/holder")
@RequiredArgsConstructor
public class HolderController {

    private final HolderService holderService;

    private final HolderDetailsConverter holderDetailsConverter;

    @GetMapping
    public String getAll() {
        return holderService.getAllHolders();
    }

    @PostMapping(path = "/add")
    public HolderDetails newBook(@Valid @RequestBody final HolderDto dto) {
        return holderDetailsConverter.convert(holderService.addHolder(dto));
    }

    @PatchMapping("/{id}/edit")
    public HolderDetails editBook(@NotBlank @PathVariable("id") final String id, @Valid @RequestBody final HolderUpdateRequest holderUpdateRequest) {
        return holderDetailsConverter.convert(holderService.editHolder(id, holderUpdateRequest));
    }

    @GetMapping("/{id}/delete")
    public boolean deleteBook(@NotBlank @PathVariable("id") final String id) {
        return holderService.deleteHolder(id);
    }

}
