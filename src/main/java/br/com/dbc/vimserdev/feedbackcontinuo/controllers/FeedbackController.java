package br.com.dbc.vimserdev.feedbackcontinuo.controllers;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCompleteDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.interfaces.documentation.FeedbackAPI;
import br.com.dbc.vimserdev.feedbackcontinuo.services.FeedbackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@Validated
@RequiredArgsConstructor
@Api(value = "Feedback")
public class FeedbackController implements FeedbackAPI {

    private final FeedbackService feedbackService;

    @PostMapping
    public ResponseEntity<FeedbackDTO> create(@RequestBody FeedbackCreateDTO createDTO) throws BusinessRuleException, JsonProcessingException {
        FeedbackDTO created = feedbackService.create(createDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/receveid")
    public ResponseEntity<Page<FeedbackCompleteDTO>> getReceveidFeedbacks(@RequestParam Integer page) throws BusinessRuleException {
        return new ResponseEntity<>(feedbackService.getReceivedFeedbacks(page), HttpStatus.OK);
    }

    @GetMapping("/gived")
    public ResponseEntity<Page<FeedbackCompleteDTO>> getGivedFeedbacks(@RequestParam Integer page) throws BusinessRuleException {
        return new ResponseEntity<>(feedbackService.getGivedFeedbacks(page), HttpStatus.OK);
    }
}
