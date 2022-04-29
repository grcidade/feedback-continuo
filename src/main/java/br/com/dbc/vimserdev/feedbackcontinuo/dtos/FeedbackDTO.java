package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FeedbackDTO extends FeedbackCreateDTO {

    private String feedbackId;
    private String userId;
    private String createdAt;

}
