package br.com.dbc.vimserdev.feedbackcontinuo.controllers;

import br.com.dbc.vimserdev.feedbackcontinuo.dtos.LoginDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserCreateDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.dtos.UserDTO;
import br.com.dbc.vimserdev.feedbackcontinuo.exception.BusinessRuleException;
import br.com.dbc.vimserdev.feedbackcontinuo.security.TokenService;
import br.com.dbc.vimserdev.feedbackcontinuo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @PostMapping("/sign-in")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                );

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        String token = tokenService.getToken(authenticate);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }


    @PostMapping("/sign-up")
    public ResponseEntity<String> register(@RequestBody @Valid UserCreateDTO userCreateDTO) throws BusinessRuleException {
        userService.create(userCreateDTO);
        LoginDTO login = new LoginDTO();
        login.setEmail(userCreateDTO.getEmail());
        login.setPassword(userCreateDTO.getPassword());
        return login(login);
    }

}
