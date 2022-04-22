package br.com.dbc.vimserdev.feedbackcontinuo.services;

import br.com.dbc.vimserdev.feedbackcontinuo.repositories.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

}
