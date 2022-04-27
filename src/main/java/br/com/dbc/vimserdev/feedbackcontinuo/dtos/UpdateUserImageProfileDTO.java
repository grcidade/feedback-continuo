package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserImageProfileDTO {

    private Boolean isTrue;
    private String image;
}
