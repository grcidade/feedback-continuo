package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import lombok.Data;

@Data
public class FeedbackDTO extends FeedbackCreateDTO {

    private String feedbackId;
    private String userId;
    private String createdAt;

}
