package br.com.dbc.vimserdev.feedbackcontinuo.controllers;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/loged-user-id")
    public ResponseEntity<String> getLogedUserId(){
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
    public ResponseEntity<String> updatePasswordUserLoged(@RequestParam String newPassword) throws BusinessRuleException {
        userService.changePasswordUserLoged(newPassword);
        return new ResponseEntity<>("Senha alterada com sucesso!", HttpStatus.ACCEPTED);
    }

    @PutMapping("/update-profile-image")
    public ResponseEntity<String> updateProfileImageUserLoged(@RequestParam String newProfileImage) {
        userService.changeProfileImageUserLoged(newProfileImage);
        return new ResponseEntity<>("Imagem alterada com sucesso!", HttpStatus.ACCEPTED);
    }

}
