package br.com.dbc.vimserdev.feedbackcontinuo.controllers;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCompleteDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feedback")
@Validated
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public FeedbackDTO create(@RequestBody FeedbackCreateDTO createDTO) throws BusinessRuleException {
        return feedbackService.create(createDTO);
    }

    @GetMapping("/receveid")
    public Page<FeedbackCompleteDTO> getReceveidFeedbacks(@RequestParam Integer page) throws BusinessRuleException {
        return feedbackService.getReceivedFeedbacks(page);
    }

    @GetMapping("/gived")
    public Page<FeedbackCompleteDTO> getGivedFeedbacks(@RequestParam Integer page) throws BusinessRuleException {
        return feedbackService.getGivedFeedbacks(page);
    }
}
