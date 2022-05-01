package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class FeedbackCompleteDTO {

    @ApiModelProperty(value = "Id do feedback")
    private String feedbackId;
    @ApiModelProperty(value = "Nome do usuário que enviou ou recebeu o feedback.")
    private String userName;
    @ApiModelProperty(value = "Foto do usuário que enviou ou recebeu o feedback.")
    private String profileUserImage;
    @ApiModelProperty(value = "Mensagem do feedback.")
    private String message;
    @ApiModelProperty(value = "Lista de tags.")
    private List<String> tags;
    @ApiModelProperty(value = "Data de criação do feedback.")
    private LocalDateTime createdAt;

}
