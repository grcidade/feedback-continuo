package br.com.dbc.vimserdev.feedbackcontinuo.controllers;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCompleteDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feedback")
@Validated
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public FeedbackDTO create(FeedbackCreateDTO createDTO) throws BusinessRuleException {
        return feedbackService.create(createDTO);
    }

    @GetMapping("/receveid")
    public List<FeedbackCompleteDTO> getReceveidFeedbacks() throws BusinessRuleException {
        return feedbackService.getReceivedFeedbacks();
    }

    @GetMapping("/gived")
    public List<FeedbackCompleteDTO> getGivedFeedbacks() throws BusinessRuleException {
        return feedbackService.getGivedFeedbacks();
    }
}
