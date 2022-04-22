package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import lombok.Data;

@Data
public class UserDTO {

    private String userId;
    private String name;
    private String email;
    private String profileImage;
}
