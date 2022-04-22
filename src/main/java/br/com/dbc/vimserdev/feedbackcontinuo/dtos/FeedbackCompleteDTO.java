package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FeedbackCompleteDTO {

    private String feedbackId;
    private String userName;
    private String profileUserImage;
    private String message;
    private List<String> tags;
    private LocalDateTime createdAt;

}
