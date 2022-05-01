package br.com.dbc.vimserdev.feedbackcontinuo.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TagDTO {

    @ApiModelProperty(value = "Id da tag.")
    private Integer tagId;
    @ApiModelProperty(value = "Nome da tag.")
    private String name;
}
