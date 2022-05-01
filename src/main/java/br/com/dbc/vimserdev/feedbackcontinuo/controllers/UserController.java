package br.com.dbc.vimserdev.feedbackcontinuo.controllers;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.ChangePasswordDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.interfaces.documentation.UserAPI;
import br.com.dbc.vimserdev.feedbackcontinuo.services.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
@Api(value = "User")
public class UserController implements UserAPI {

    private final UserService userService;

    @GetMapping("/loged-user-id")
    public ResponseEntity<String> getLogedUserId() {
        return new ResponseEntity<>(userService.getLogedUserId(), HttpStatus.OK);
    }

    @GetMapping("/user-loged")
    public ResponseEntity<UserDTO> getLogedUser() {
        return new ResponseEntity<>(userService.getLogedUser(), HttpStatus.OK);
    }

    @GetMapping("/list-all-users-without-loged")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsersExceptLogedUser(), HttpStatus.OK);
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> updatePasswordUserLoged(@RequestBody @Valid ChangePasswordDTO changePasswordDTO) throws BusinessRuleException {
        userService.changePasswordUserLoged(changePasswordDTO);
        return new ResponseEntity<>("Senha alterada com sucesso!", HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/update-profile-image", consumes = {MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> updateProfileImageUserLoged(@RequestPart MultipartFile profileImage) throws BusinessRuleException {
        userService.changeProfileImageUserLoged(profileImage);
        return new ResponseEntity<>("Imagem alterada com sucesso!", HttpStatus.ACCEPTED);
    }

}
