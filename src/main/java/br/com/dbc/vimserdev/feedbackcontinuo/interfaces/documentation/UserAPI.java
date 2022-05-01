package br.com.dbc.vimserdev.feedbackcontinuo.interfaces.documentation;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.ChangePasswordDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Api
@Validated
public interface UserAPI {

    @ApiOperation(value = "Busca o id do usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna o id do usuário logado."),
            @ApiResponse(code = 401, message = "Não é possível atribuir feedbacks a si mesmo."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção.")
    })
    ResponseEntity<String> getLogedUserId();

    @ApiOperation(value = "Busca o usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna o usuário logado."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção.")
    })
    ResponseEntity<UserDTO> getLogedUser();

    @ApiOperation(value = "Busca todos os usuários.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista com todos os usuários."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção.")
    })
    public ResponseEntity<List<UserDTO>> getAllUsers();

    @ApiOperation(value = "Atualiza o senha do usuário.")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Retorna uma mensagem."),
            @ApiResponse(code = 401, message = "Senha inválida."),
            @ApiResponse(code = 401, message = "Senha antiga incompátivel."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção.")
    })
    ResponseEntity<String> updatePasswordUserLoged(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) throws BusinessRuleException;

    @ApiOperation(value = "Atualiza a imagem do usuário.")
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Retorna uma mensagem."),
            @ApiResponse(code = 401, message = "Senha inválida."),
            @ApiResponse(code = 406, message = "Tipo de arquivo não suportado."),
            @ApiResponse(code = 417, message = "Erro ao salvar imagem."),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção.")
    })
    ResponseEntity<String> updateProfileImageUserLoged(@RequestPart MultipartFile profileImage) throws BusinessRuleException;
}
