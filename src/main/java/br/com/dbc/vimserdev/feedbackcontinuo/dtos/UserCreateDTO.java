package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {

    @NotEmpty
    private String name;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    private String password;

}
