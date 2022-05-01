package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LoginDTO {

    @ApiModelProperty(value = "Email do usuário.")
    @NotNull
    @NotEmpty
    private String email;

    @ApiModelProperty(value = "Senha do usuário.")
    @NotNull
    @NotEmpty
    private String password;
}
