package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class ChangePasswordDTO {

    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;
}
