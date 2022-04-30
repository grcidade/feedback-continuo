package br.com.dbc.vimserdev.feedbackcontinuo.interfaces.documentation;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.LoginDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Api
@Validated
public interface AuthAPI {

    @ApiOperation(value = "Logar com usuário")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna um token de autenticação"),
            @ApiResponse(code = 403, message = "Você não tem permissão para acessar este recurso"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção")
    })
    ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO);

    @ApiOperation(value = "Cria um novo usuário usuário.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna um token de autenticação."),
            @ApiResponse(code = 400, message = "Senha fraca."),
            @ApiResponse(code = 401, message = "Email inválido ou já existente."),
            @ApiResponse(code = 406, message = "Tipo de arquivo não suportado."),
            @ApiResponse(code = 417, message = "Erro ao salvar imagem."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção.")
    })
    ResponseEntity<String> register(@Valid @ModelAttribute UserCreateDTO userCreateDTO, @RequestPart(required = false) MultipartFile profileImage) throws BusinessRuleException;

    @ApiOperation(value = "Envia uma nova senha para o email.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma mensagem."),
            @ApiResponse(code = 400, message = "Email inválido."),
            @ApiResponse(code = 404, message = "Usuário não encontrado."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção.")
    })
    ResponseEntity<String> forgotPassowrd(@PathVariable("email") String email) throws BusinessRuleException, JsonProcessingException;
}
