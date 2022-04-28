package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailHandlerDTO {

    private String to;
    private String from;
}
