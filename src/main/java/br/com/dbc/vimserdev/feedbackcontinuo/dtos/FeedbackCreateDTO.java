package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import br.com.dbc.vimserdev.feedbackcontinuo.entities.TagEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class FeedbackCreateDTO {

    @NotEmpty
    private String message;
    private Boolean isAnonymous;
//    private List<TagEntity> tags;
    @NotEmpty
    @Size(min = 36, max = 36)
    private String feedbackUserId;
}
