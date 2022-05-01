package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

    @ApiModelProperty(value = "Nome do usuário.")
    @NotEmpty
    private String name;
    @ApiModelProperty(value = "Email do usuário.")
    @NotEmpty
    @Email
    private String email;
    @ApiModelProperty(value = "Senha do usuário.")
    @NotEmpty
    private String password;

}
