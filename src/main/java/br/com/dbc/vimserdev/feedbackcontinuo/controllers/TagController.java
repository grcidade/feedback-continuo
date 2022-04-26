package br.com.dbc.vimserdev.feedbackcontinuo.controllers;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.TagDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tag")
@Validated
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return new ResponseEntity<>(tagService.getAllTags(), HttpStatus.OK);
    }

}
