package br.com.dbc.vimserdev.feedbackcontinuo.controllers;

import br.com.dbc.vimserdev.feedbackcontinuo.services.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feedback")
@Validated
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

}
