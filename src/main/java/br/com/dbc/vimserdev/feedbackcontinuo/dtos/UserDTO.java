package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String userId;
    private String name;
    private String email;
    private String profileImage;
}
