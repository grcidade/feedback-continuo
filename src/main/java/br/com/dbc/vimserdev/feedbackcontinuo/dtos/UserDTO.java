package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @ApiModelProperty(value = "Id do usuário.")
    private String userId;
    @ApiModelProperty(value = "Nome do usuário.")
    private String name;
    @ApiModelProperty(value = "Email do usuário.")
    private String email;
    @ApiModelProperty(value = "Imagem em base64 do usuário.")
    private String profileImage;
}
