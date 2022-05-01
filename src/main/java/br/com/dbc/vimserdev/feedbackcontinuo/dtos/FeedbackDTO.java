package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FeedbackDTO extends FeedbackCreateDTO {

    @ApiModelProperty(value = "Id do feedback.")
    private String feedbackId;
    @ApiModelProperty(value = "Id do usuário que criou o feedback.")
    private String userId;
    @ApiModelProperty(value = "Data de criação do feedback.")
    private String createdAt;

}
