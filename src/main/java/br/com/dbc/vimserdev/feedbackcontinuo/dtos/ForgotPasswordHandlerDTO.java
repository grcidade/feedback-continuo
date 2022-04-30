package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ForgotPasswordHandlerDTO {

    private String code;
    private String to;
}
