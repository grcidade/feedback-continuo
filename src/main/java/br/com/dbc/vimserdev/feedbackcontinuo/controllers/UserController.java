package br.com.dbc.vimserdev.feedbackcontinuo.controllers;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/loged-user-id")
    public String getLogedUserId(){
        return userService.getLogedUserId();
    }

    @GetMapping("/user-loged")
    public UserDTO getLogedUser() throws BusinessRuleException {
        return userService.getLogedUser();
    }

    @GetMapping("/list-all-users")
    public List<UserDTO> getAllUsers(){
        return userService.getAllUsers();
    }

}
