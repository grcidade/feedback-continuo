package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import br.com.dbc.vimserdev.feedbackcontinuo.enums.Tags;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FeedbackCreateDTO {

    @ApiModelProperty(value = "Mensagem do feedback.")
    @NotEmpty
    private String message;
    @ApiModelProperty(value = "Autenticidade do feedback.")
    private Boolean isAnonymous;
    @ApiModelProperty(value = "Lista de tags.")
    private List<Tags> tags;
    @ApiModelProperty("Id do usuário que recebeu o feedback.")
    @NotEmpty
    @Size(min = 36, max = 36)
    private String feedbackUserId;
}
