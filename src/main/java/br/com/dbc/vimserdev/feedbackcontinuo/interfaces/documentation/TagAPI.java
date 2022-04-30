package br.com.dbc.vimserdev.feedbackcontinuo.interfaces.documentation;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.TagDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Api
public interface TagAPI {

    @ApiOperation(value = "Busca as tags.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista de tags."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção.")
    })
    ResponseEntity<List<TagDTO>> getAllTags();
}
