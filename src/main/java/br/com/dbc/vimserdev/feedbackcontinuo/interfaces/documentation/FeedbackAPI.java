package br.com.dbc.vimserdev.feedbackcontinuo.interfaces.documentation;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCompleteDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.FeedbackDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Api
@Validated
public interface FeedbackAPI {

    @ApiOperation(value = "Cria um novo feedback.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Retorna o feedback criado."),
            @ApiResponse(code = 401, message = "Não é possível atribuir feedbacks a si mesmo."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção.")
    })
    ResponseEntity<FeedbackDTO> create(@RequestBody FeedbackCreateDTO createDTO) throws BusinessRuleException, JsonProcessingException;

    @ApiOperation(value = "Busca os feedbacks recebidos.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista paginada dos feedbacks recebidos."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção.")
    })
    ResponseEntity<Page<FeedbackCompleteDTO>> getReceveidFeedbacks(@RequestParam Integer page) throws BusinessRuleException;

    @ApiOperation(value = "Busca os feedbacks enviados.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista paginada dos feedbacks enviados."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção.")
    })
    ResponseEntity<Page<FeedbackCompleteDTO>> getGivedFeedbacks(@RequestParam Integer page) throws BusinessRuleException;
}
